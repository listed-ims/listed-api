package com.citu.listed.user.security;

import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service("MethodSecurity")
@RequiredArgsConstructor
public class MethodSecurityService {
    Authentication authentication;

    public boolean hasPermission(String permission) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        User user = (User) authentication.getPrincipal();
        Integer storeId = user.getCurrentStoreId();

        if (storeId == null) {
            return false;
        }

        SimpleGrantedAuthority requiredAuthority = new SimpleGrantedAuthority(storeId + "_" + permission);
        SimpleGrantedAuthority ownerAuthority = new SimpleGrantedAuthority(storeId + "_" + UserPermissions.OWNER);

        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.equals(ownerAuthority) || authority.equals(requiredAuthority));
    }

    public boolean hasAnyPermission(String... permissions) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getCurrentStoreId() == null) {
            return false;
        }

        Integer storeId = user.getCurrentStoreId();

        Set<SimpleGrantedAuthority> authorities = Arrays.stream(permissions)
                .map(permission -> new SimpleGrantedAuthority(storeId + "_" + permission))
                .collect(Collectors.toSet());

        SimpleGrantedAuthority ownerAuthority = new SimpleGrantedAuthority(storeId + "_" + UserPermissions.OWNER);

        return user.getAuthorities().contains(ownerAuthority)
                || user.getAuthorities().stream().anyMatch(authorities::contains);

    }
}
