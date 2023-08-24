package com.citu.listed.store;

import com.citu.listed.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    List<Store> findAllByMembersUser(User user, Pageable pageable);
    List<Store> findAllByMembersUserAndStatus(User user, StoreStatus status, Pageable pageable);
}
