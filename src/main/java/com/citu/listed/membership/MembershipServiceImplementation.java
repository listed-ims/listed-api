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

import java.util.*;
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
            Integer userId,
            int pageNumber,
            int pageSize
    ) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found."));
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("user.name"));
        List<Membership> memberships;

        if (user == null) {
            if (membershipStatus == null) {
                memberships = membershipRepository.findByStore(store, pageable);
            } else {
                memberships = membershipRepository.
                        findByStoreAndMembershipStatus(store, membershipStatus, pageable);
            }
        } else {
            if (membershipStatus == null) {
                memberships = Collections.singletonList(
                        membershipRepository.findByStoreAndUser(store, user)
                );
            } else {
                memberships = Collections.singletonList(
                        membershipRepository.findByStoreAndMembershipStatusAndUser(store, membershipStatus, user));
            }
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
    @Transactional
    public MembershipResponse updateCollaborator(
            Integer id,
            Set<UserPermissions> userPermissions,
            MembershipStatus membershipStatus
    ) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership does not exist."));

        if (membership.getPermissions().contains(
                permissionRepository.findByUserPermission(UserPermissions.OWNER)
        )) {
            throw new BadRequestException("Cannot update owner membership.");
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
            if (Objects.equals(user.getCurrentStoreId(), membership.getStore().getId())) {
                user.setCurrentStoreId(null);
                userRepository.save(user);
            }
            membership.setPermissions(new HashSet<>());
            membership.setMembershipStatus(membershipStatus);
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
        }

        membership.setMembershipStatus(membershipStatus);
        Membership updatedMembership = membershipRepository.save(membership);

        if (membershipStatus == MembershipStatus.INACTIVE) {
            notificationService.addNewNotification(
                    membership,
                    null,
                    null,
                    membership.getSender(), NotificationType.COLLABORATOR_REMOVAL);
        } else if (membershipStatus == MembershipStatus.PENDING) {
            notificationService.addNewNotification(
                    membership,
                    null,
                    null,
                    membership.getSender(),
                    NotificationType.STORE_INVITE);
        } else {
            notificationService.addNewNotification(
                    membership,
                    null,
                    null,
                    membership.getSender(),
                    NotificationType.INVITE_REPLY);
        }

        return membershipResponseMapper.apply(updatedMembership);
    }
}
