package com.citu.listed.user.dtos;

public record UserResponse(
        Integer id,
        String name,
        String username,
        Integer currentStoreId
) {
}
