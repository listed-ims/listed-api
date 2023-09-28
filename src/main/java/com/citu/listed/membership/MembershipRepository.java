package com.citu.listed.membership;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.store.Store;
import com.citu.listed.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    boolean existsByStoreAndUser(Store store, User user);
    List<Membership> findByStoreAndMembershipStatus(Store store, MembershipStatus membershipStatus, Pageable pageable);
    List<Membership> findByStore(Store store, Pageable pageable);
}
