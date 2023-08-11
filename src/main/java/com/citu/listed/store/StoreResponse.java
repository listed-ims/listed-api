package com.citu.listed.store;

public record StoreResponse(
        Integer id,
        String name,
        StoreStatus status
) {
}
