package com.citu.listed.incoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    List<Incoming> findByProductStoreId(Integer storeId);

    @Query(
            "SELECT COUNT(incoming) " +
                    "FROM Incoming incoming " +
                    "WHERE DATE(incoming.transactionDate) = DATE(:transactionDate)"
    )
    Long countByTransactionDate(LocalDateTime transactionDate);

    @Query(
            "SELECT COALESCE(SUM(incoming.actualQuantity), 0) " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.product.store.id = :storeId"
    )
    Double getTotalProductsByStoreId(Integer storeId);

    @Query(
            "SELECT COALESCE(SUM(incoming.actualQuantity * incoming.purchasePrice), 0) " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.product.store.id = :storeId"
    )
    Double getTotalPriceValueByStoreId(Integer storeId);

    @Query(
            "SELECT COALESCE(SUM(incoming.actualQuantity), 0) " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.product.id = :productId"
    )
    Double getTotalQuantityByProductId(Integer productId);

    @Query(
            "SELECT COALESCE(SUM(incoming.initialQuantity), 0) " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.product.id = :productId"
    )
    Double getTotalInByProductId(Integer productId);

    @Query(
            "SELECT incoming FROM Incoming incoming " +
                    "WHERE incoming.actualQuantity > 0 " +
                    "AND incoming.product.id = :productId " +
                    "ORDER BY incoming.transactionDate LIMIT 1"
    )
    Optional<Incoming> getEarliestByProductId(Integer productId);
}
