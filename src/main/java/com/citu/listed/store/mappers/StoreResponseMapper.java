package com.citu.listed.store.mappers;

import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.store.Store;
import com.citu.listed.store.dtos.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StoreResponseMapper implements Function<Store, StoreResponse> {

    private final IncomingRepository incomingRepository;

    @Override
    public StoreResponse apply(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getStatus(),
                incomingRepository.getTotalProductsByStoreId(store.getId()),
                incomingRepository.getTotalPriceValueByStoreId(store.getId())
        );
    }
}
