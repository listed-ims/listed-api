package com.citu.listed.store;

import com.citu.listed.exception.NotFoundException;
import com.citu.listed.membership.Membership;
import com.citu.listed.membership.MembershipRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImplementation implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private JwtService jwtService;

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
}
