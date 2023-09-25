package com.citu.listed.store.dtos;

import com.citu.listed.store.enums.StoreStatus;

public record StoreResponse(
        Integer id,
        String name,
        StoreStatus status,
        Double totalProducts,
        Double totalPriceValue,
        Long totalLowStock,
        Double totalNearExpiry,
        Double totalRevenue,
        Double totalItemsSold
) {
}
