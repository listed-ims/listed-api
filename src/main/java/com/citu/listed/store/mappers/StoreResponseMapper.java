package com.citu.listed.store.mappers;

import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.outgoing.OutgoingRepository;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.store.Store;
import com.citu.listed.store.dtos.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StoreResponseMapper implements Function<Store, StoreResponse> {

    private final IncomingRepository incomingRepository;
    private final ProductRepository productRepository;
    private final OutgoingRepository outgoingRepository;

    @Override
    public StoreResponse apply(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getStatus(),
                incomingRepository.getTotalProductsByStoreId(store.getId()),
                incomingRepository.getTotalPriceValueByStoreId(store.getId()),
                productRepository.countLowStockProductsByStoreId(store.getId()),
                incomingRepository.getTotalNearExpiryItemsByStoreId(store.getId(), LocalDate.now().plusDays(14)),
                outgoingRepository.getTotalRevenueByStoreId(LocalDate.now(), store.getId(), OutgoingCategory.SALES),
                outgoingRepository.getTotalItemsSoldToday(LocalDate.now(), store.getId(), OutgoingCategory.SALES)
        );
    }
}
