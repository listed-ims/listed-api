package com.citu.listed.permission;

import com.citu.listed.permission.enums.UserPermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements ApplicationListener<ApplicationReadyEvent>{
    private final PermissionRepository permissionRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        Set<UserPermissions> existingPermissions = permissionRepository.findAll()
                .stream()
                .map(Permission::getUserPermission)
                .collect(Collectors.toSet());

        List<Permission> missingPermissions = Arrays.stream(UserPermissions.values())
                .filter(userPermission -> !existingPermissions.contains(userPermission))
                .map(userPermission -> {
                    Permission permission = new Permission();
                    permission.setUserPermission(userPermission);
                    return permission;
                })
                .toList();

        permissionRepository.saveAll(missingPermissions);
    }
}
