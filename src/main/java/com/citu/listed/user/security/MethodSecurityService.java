package com.citu.listed.user.security;

import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service("MethodSecurity")
@RequiredArgsConstructor
public class MethodSecurityService {
    Authentication authentication;
    HttpServletRequest request;

    public boolean hasPermission(String permission) {
        request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getCurrentStoreId() + "_" + permission);
        SimpleGrantedAuthority ownerAuthority = new SimpleGrantedAuthority(user.getCurrentStoreId() + "_" + UserPermissions.OWNER);

        return user.getAuthorities().contains(ownerAuthority) ||
                user.getAuthorities().contains(authority);

    }

    public boolean hasAnyPermission(String... permissions) {
        request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        SimpleGrantedAuthority ownerAuthority = new SimpleGrantedAuthority(user.getCurrentStoreId() + "_" + UserPermissions.OWNER);
        
        for (String permission : permissions) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getCurrentStoreId() + "_" + permission);

            if (user.getAuthorities().contains(ownerAuthority) ||
                    user.getAuthorities().contains(authority)) {
                return true;
            }
        }

        return false;
    }
}
