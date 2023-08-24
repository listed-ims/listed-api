package com.citu.listed.store;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StoreResponseMapper implements Function<Store, StoreResponse> {

    @Override
    public StoreResponse apply(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getStatus()
        );
    }
}
