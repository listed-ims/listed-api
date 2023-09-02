package com.citu.listed.store;

import java.util.List;

public interface StoreService {

    List<StoreResponse> getStores(
            String token,
            StoreStatus status,
            int pageNumber,
            int pageSize);
    StoreResponse getStore(String token, Integer id);
    void createStore(String token, Store store);
    void updateStore(Integer id, Store store);
}
