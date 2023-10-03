package com.citu.listed.store.dtos;

import com.citu.listed.store.enums.StoreStatus;
import com.citu.listed.user.dtos.UserResponse;

public record StoreResponse(
        Integer id,
        String name,
        UserResponse owner,
        StoreStatus status,
        Double totalProducts,
        Double totalPriceValue,
        Long totalLowStock,
        Double totalNearExpiry,
        Double totalRevenue,
        Double totalItemsSold
) {
}
