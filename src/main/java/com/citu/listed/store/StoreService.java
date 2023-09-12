package com.citu.listed.store;

import java.util.List;

public interface StoreService {

    List<StoreResponse> getStores(
            String token,
            StoreStatus status,
            int pageNumber,
            int pageSize);
    StoreResponse getStore(String token, Integer id);
    StoreResponse createStore(String token, Store store);
    StoreResponse updateStore(Integer id, Store store);
}
