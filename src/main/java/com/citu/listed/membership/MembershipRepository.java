package com.citu.listed.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    boolean existsByStore_IdAndUser_Id(Integer storeId, Integer userId);

}
