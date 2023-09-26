package com.citu.listed.permission;

import com.citu.listed.permission.enums.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByUserPermission(UserPermissions userPermission);
}
