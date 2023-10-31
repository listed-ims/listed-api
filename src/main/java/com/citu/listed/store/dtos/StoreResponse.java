package com.citu.listed.store.dtos;

import com.citu.listed.user.dtos.UserResponse;

public record StoreResponse(
        Integer id,
        String name,
        UserResponse owner,
        Double inventoryValue
) {
}
