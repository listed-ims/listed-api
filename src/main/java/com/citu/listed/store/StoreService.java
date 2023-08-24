package com.citu.listed.store;

import java.util.List;

public interface StoreService {

    List<StoreResponse> getStores(
            String token,
            StoreStatus status,
            int pageNumber,
            int pageSize);
    void createStore(String token, Store store);
}
