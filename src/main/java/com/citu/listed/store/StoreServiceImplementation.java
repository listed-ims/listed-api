package com.citu.listed.store;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.PermissionRepository;
import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.membership.Membership;
import com.citu.listed.membership.MembershipRepository;
import com.citu.listed.store.dtos.StoreResponse;
import com.citu.listed.store.enums.StoreStatus;
import com.citu.listed.store.mappers.StoreResponseMapper;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImplementation implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;
    private final StoreResponseMapper storeResponseMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public List<StoreResponse> getStores(
            String token,
            StoreStatus status,
            int pageNumber,
            int pageSize
    ) {
        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        List<Store> stores;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        if(status == null)
            stores = storeRepository.findAllByMembersUser(user, pageable);
        else
            stores = storeRepository.findAllByMembersUserAndStatus(user, status, pageable);

        return stores.stream()
                .map(storeResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public StoreResponse getStore(String token, Integer id) {
        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Optional<Store> store = storeRepository.findByIdAndMembersUser(id, user);

        return store.map(storeResponseMapper)
                .orElseThrow(() -> new NotFoundException("Store not found."));
    }

    @Override
    @Transactional
    public StoreResponse createStore(String token, Store store) {
        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Store newStore = storeRepository.save(
                Store.builder()
                        .name(store.getName())
                        .status(StoreStatus.OPEN)
                        .build());

        Membership membership = membershipRepository.save(
                Membership.builder()
                        .store(newStore)
                        .user(user)
                        .membershipStatus(MembershipStatus.ACCEPTED)
                        .permissions(Collections.singleton(permissionRepository.findByUserPermission(UserPermissions.OWNER)))
                        .build());

        if(user.getCurrentStoreId() == null)
            user.setCurrentStoreId(newStore.getId());

        return storeResponseMapper.apply(newStore);
    }

    @Override
    public StoreResponse updateStore(Integer id, Store store) {
        Store storeToUpdate = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store to update not found."));

        storeToUpdate.setName(store.getName());
        storeToUpdate.setStatus(store.getStatus());

        return storeResponseMapper.apply(storeRepository.save(storeToUpdate));
    }

}
