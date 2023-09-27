package com.citu.listed.membership.dtos;

import com.citu.listed.permission.enums.UserPermissions;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipRequest {

    @NotNull(message = "Store ID is required")
    private Integer storeId;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Permission/s is/are required")
    private Set<UserPermissions> userPermissions;
}

