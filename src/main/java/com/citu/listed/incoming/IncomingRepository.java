package com.citu.listed.incoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("SELECT COUNT(incoming) FROM Incoming incoming WHERE DATE(incoming.transactionDate) = DATE(:transactionDate)")
    Long countByTransactionDate(LocalDateTime transactionDate);

    @Query("SELECT SUM(incoming.actualQuantity) FROM Incoming incoming WHERE incoming.product.store.id = :storeId")
    Double getTotalProductsByStoreId(Integer storeId);

    @Query("SELECT SUM(incoming.actualQuantity * incoming.purchasePrice) FROM Incoming incoming WHERE incoming.product.store.id = :storeId")
    Double getTotalPriceValueByStoreId(Integer storeId);
}
