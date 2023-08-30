package com.citu.listed.store;

import com.citu.listed.exception.NotFoundException;
import com.citu.listed.membership.Membership;
import com.citu.listed.membership.MembershipRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        if(status == null) {
            stores = storeRepository.findAllByMembersUser(user, pageable);
        } else {
            stores = storeRepository.findAllByMembersUserAndStatus(user, status, pageable);
        }

        return stores.stream()
                .map(storeResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public StoreResponse getStore(Integer id) {
        Optional<Store> store = storeRepository.findById(id);

        return store.map(storeResponseMapper)
                .orElseThrow(() -> new NotFoundException("Store not found."));
    }

    @Override
    @Transactional
    public void createStore(String token, Store store) {
        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Store newStore = Store.builder()
                .name(store.getName())
                .status(StoreStatus.OPEN)
                .build();

        Membership membership = Membership.builder()
                .store(storeRepository.save(newStore))
                .user(user)
                .build();

        membershipRepository.save(membership);
    }

    @Override
    public void updateStore(Integer id, Store store) {
        Store storeToUpdate = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store to update not found."));

        storeToUpdate.setName(store.getName());
        storeToUpdate.setStatus(store.getStatus());

        storeRepository.save(storeToUpdate);
    }

}
