package com.citu.listed.store;

import com.citu.listed.store.enums.StoreStatus;
import com.citu.listed.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    Optional<Store> findByIdAndMembersUser(Integer id, User user);
    List<Store> findAllByMembersUser(User user, Pageable pageable);
    List<Store> findAllByMembersUserAndStatus(User user, StoreStatus status, Pageable pageable);
    Optional<Store> findFirstByMembersUserAndStatusAndIdNot(User user, StoreStatus status, Integer id);

}
