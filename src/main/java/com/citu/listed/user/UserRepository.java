package com.citu.listed.user;

import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    User findByMemberships_StoreAndMemberships_Permissions_UserPermission(Store store, UserPermissions userPermission);
    List<User> findByCurrentStoreId(Integer storeId);

}