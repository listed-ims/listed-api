package com.citu.listed.analytics;

import com.citu.listed.analytics.dtos.SummaryResponse;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.outgoing.OutgoingRepository;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImplementation implements AnalyticsService{

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final IncomingRepository incomingRepository;
    private final OutgoingRepository outgoingRepository;

    @Override
    public SummaryResponse getSummary(Integer id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        return new SummaryResponse(
                productRepository.countLowStockProductsByStoreId(store.getId()),
                incomingRepository.getTotalNearExpiryItemsByStoreId(store.getId(), LocalDate.now().plusDays(14)),
                outgoingRepository.getTotalRevenueByStoreId(store.getId(), OutgoingCategory.SALES, LocalDate.now(), LocalDate.now()),
                outgoingRepository.getTotalItemsSoldByStoreId(store.getId(), OutgoingCategory.SALES, LocalDate.now(), LocalDate.now())
        );
    }
}
