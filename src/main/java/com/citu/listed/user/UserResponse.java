package com.citu.listed.user;

public record UserResponse(
        Integer id,
        String name,
        String username,
        Integer currentStoreId
) {
}
