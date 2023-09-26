package com.citu.listed.permission;

import com.citu.listed.permission.enums.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
