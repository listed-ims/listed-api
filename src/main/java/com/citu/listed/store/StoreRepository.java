package com.citu.listed.store;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.store.enums.StoreStatus;
import com.citu.listed.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    Optional<Store> findByIdAndMembersUserAndMembersMembershipStatusNot(Integer id, User user, MembershipStatus membershipStatus);
    List<Store> findByMembersUserAndMembersMembershipStatusNot(User user, MembershipStatus membershipStatus, Pageable pageable);
    List<Store> findByMembersUserAndStatusAndMembersMembershipStatusNot(User user, StoreStatus status, MembershipStatus membershipStatus, Pageable pageable);
    Optional<Store> findFirstByMembersUserAndStatusAndIdNotAndMembersMembershipStatusNot(User user, StoreStatus status, Integer id, MembershipStatus membershipStatus);

}
