package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.membership.mappers.MembershipResponseMapper;
import com.citu.listed.notification.NotificationService;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.permission.Permission;
import com.citu.listed.permission.PermissionRepository;
import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.shared.exception.BadRequestException;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipServiceImplementation implements MembershipService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MembershipRepository membershipRepository;
    private final PermissionRepository permissionRepository;
    private final MembershipResponseMapper membershipResponseMapper;
    private final NotificationService notificationService;
    private final JwtService jwtService;

    @Override
    @Transactional
    public MembershipResponse addCollaborator(String token, MembershipRequest membership) {

        User sender = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("Sender not found."));

        User user = userRepository.findByUsername(membership.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Store store = storeRepository.findById(membership.getStoreId())
                .orElseThrow(() -> new NotFoundException(("Store not found.")));

        if (membershipRepository.existsByStoreAndUser(store, user)) {
            throw new BadRequestException("User is already a collaborator.");
        }

        Set<Permission> permissions = membership.getUserPermissions()
                .stream()
                .map(permissionRepository::findByUserPermission)
                .collect(Collectors.toSet());

        Membership newMembership = membershipRepository.save(
                Membership.builder()
                        .user(user)
                        .sender(sender)
                        .store(store)
                        .permissions(permissions)
                        .membershipStatus(MembershipStatus.PENDING)
                        .build());

        if (user.getCurrentStoreId() == null) {
            user.setCurrentStoreId(membership.getStoreId());
            userRepository.save(user);
        }

        notificationService.addNewNotification(
                newMembership,
                null,
                null,
                sender,
                NotificationType.STORE_INVITE);

        return membershipResponseMapper.apply(newMembership);
    }

    @Override
    public List<MembershipResponse> getCollaborators(
            Integer storeId,
            MembershipStatus membershipStatus,
            int pageNumber,
            int pageSize
    ) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("user.name"));
        List<Membership> memberships;

        if (membershipStatus == null) {
            memberships = membershipRepository.findByStore(store, pageable);
        } else {
            memberships = membershipRepository.
                    findByStoreAndMembershipStatus(store, membershipStatus, pageable);
        }

        return memberships.stream()
                .map(membershipResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipResponse getCollaborator(Integer membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NotFoundException("Membership not found."));

        return membershipResponseMapper.apply(membership);
    }

    @Override
    public MembershipResponse getMembership(String token, Integer storeId) {

        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        Membership membership = membershipRepository.findByStoreAndUserAndMembershipStatusNot(
                        store, user, MembershipStatus.INACTIVE
                )
                .orElseThrow(() -> new NotFoundException("User membership not found."));

        return membershipResponseMapper.apply(membership);
    }

    @Override
    @Transactional
    public MembershipResponse updateCollaborator(
            String token,
            Integer id,
            Set<UserPermissions> userPermissions,
            MembershipStatus membershipStatus
    ) {
        User sender = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("Sender not found."));

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership does not exist."));

        if (membership.getPermissions().contains(
                permissionRepository.findByUserPermission(UserPermissions.OWNER)
        )) {
            throw new BadRequestException("Cannot update owner membership.");
        }

        if (membershipStatus == MembershipStatus.ACTIVE) {
            throw new BadRequestException("Cannot update membership to active.");
        }

        if (userPermissions != null && !userPermissions.isEmpty()) {
            if (userPermissions.contains(UserPermissions.OWNER)) {
                throw new BadRequestException("Cannot set owner permission.");
            }
            Set<Permission> permissions = userPermissions.stream()
                    .map(permissionRepository::findByUserPermission)
                    .collect(Collectors.toSet());
            membership.setPermissions(permissions);
        }

        if (membershipStatus == null) {
            return membershipResponseMapper.apply(membershipRepository.save(membership));
        }

        User user = membership.getUser();
        if (membershipStatus == MembershipStatus.DECLINED) {
            Integer storeId = membership.getStore().getId();
            if (Objects.equals(user.getCurrentStoreId(), storeId)) {
                Store nextCurrentStore = storeRepository.
                        findFirstByMembersUserAndIdNotAndMembersMembershipStatusNot(
                                user,
                                storeId,
                                MembershipStatus.INACTIVE)
                        .orElse(null);
                user.setCurrentStoreId(nextCurrentStore != null ? nextCurrentStore.getId() : null);
                userRepository.save(user);
            }
        } else if (membershipStatus == MembershipStatus.INACTIVE) {
            Integer storeId = membership.getStore().getId();
            if (Objects.equals(user.getCurrentStoreId(), storeId)) {
                Store nextCurrentStore = storeRepository.
                        findFirstByMembersUserAndIdNotAndMembersMembershipStatusNot(
                                user,
                                storeId,
                                MembershipStatus.INACTIVE)
                        .orElse(null);
                user.setCurrentStoreId(nextCurrentStore != null ? nextCurrentStore.getId() : null);
                userRepository.save(user);
            }
            membership.setSender(sender);
        } else if (membershipStatus == MembershipStatus.PENDING) {
            membership.setSender(sender);
        }

        membership.setMembershipStatus(membershipStatus);
        Membership updatedMembership = membershipRepository.save(membership);

        if (membershipStatus != MembershipStatus.DECLINED)
            notificationService.addNewNotification(
                    membership,
                    null,
                    null,
                    updatedMembership.getSender(),
                    membershipStatus == MembershipStatus.INACTIVE
                            ? NotificationType.COLLABORATOR_REMOVAL
                            : NotificationType.STORE_INVITE
            );

        return membershipResponseMapper.apply(updatedMembership);
    }

    @Override
    public MembershipResponse acceptOrDeclineMembership(
            String token,
            Integer id,
            MembershipStatus membershipStatus
    ) {
        User invited = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership does not exist."));

        if (membership.getUser() != invited) {
            throw new BadRequestException("Cannot accept or decline other's membership.");
        }

        membership.setMembershipStatus(membershipStatus);
        Membership updatedMembership = membershipRepository.save(membership);

        notificationService.addNewNotification(
                membership,
                null,
                null,
                membership.getSender(),
                NotificationType.INVITE_REPLY
        );

        return membershipResponseMapper.apply(updatedMembership);
    }
}
