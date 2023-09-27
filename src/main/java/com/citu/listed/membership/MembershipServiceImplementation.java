package com.citu.listed.membership;

import com.citu.listed.membership.dtos.MembershipRequest;
import com.citu.listed.membership.dtos.MembershipResponse;
import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.membership.mappers.MembershipResponseMapper;
import com.citu.listed.permission.Permission;
import com.citu.listed.permission.PermissionRepository;
import com.citu.listed.shared.exception.BadRequestException;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public MembershipResponse addCollaborator(MembershipRequest membership) {

        if (membershipRepository.existsByStore_IdAndUser_Username(
                membership.getStoreId(), membership.getUsername())
        ){
            throw new BadRequestException("User is already a collaborator.");
        }

        User user = userRepository.findByUsername(membership.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Store store = storeRepository.findById(membership.getStoreId())
                .orElseThrow(()-> new NotFoundException(("Store not found.")));

        Set<Permission> permissions = membership.getUserPermissions()
                .stream()
                .map(permissionRepository::findByUserPermission)
                .collect(Collectors.toSet());

        Membership newMembership = Membership.builder()
                .user(user)
                .store(store)
                .permissions(permissions)
                .membershipStatus(MembershipStatus.PENDING)
                .build();

        return membershipResponseMapper.apply(membershipRepository.save(newMembership));
    }

    @Override
    public List<MembershipResponse> getCollaborators(
            Integer storeId,
            MembershipStatus membershipStatus,
            int pageNumber,
            int pageSize
    ){

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("user.name"));
        List<Membership> memberships;

        if (membershipStatus == null) {
            memberships = membershipRepository.findByStore(store, pageable);
        }else{
            memberships = membershipRepository.findByStoreAndMembershipStatus(
                    store, membershipStatus, pageable
            );
        }
        return memberships.stream()
                .map(membershipResponseMapper)
                .collect(Collectors.toList());
    }
}
