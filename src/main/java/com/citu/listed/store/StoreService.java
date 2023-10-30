package com.citu.listed.store;

import com.citu.listed.store.dtos.StoreResponse;

import java.util.List;

public interface StoreService {

    List<StoreResponse> getStores(
            String token,
            int pageNumber,
            int pageSize);
    StoreResponse getStore(String token, Integer id);
    StoreResponse createStore(String token, Store store);
}
