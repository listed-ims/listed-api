package com.citu.listed.incoming;

import com.citu.listed.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query(
            "SELECT incoming " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.product.store = :store " +
                    "AND (:startDate IS NULL OR DATE(incoming.transactionDate) >= :startDate) " +
                    "AND (:endDate IS NULL OR DATE(incoming.transactionDate) <= :endDate) " +
                    "AND (:productId IS NULL OR incoming.product.id = :productId) " +
                    "AND (:#{#userIds == null} = true OR incoming.user.id IN :userIds)"
    )
    List<Incoming> getByStoreId(
            Store store,
            List<Integer> userIds,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

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

    Optional<Incoming> findFirstByActualQuantityGreaterThanAndProductIdOrderByExpirationDateDesc(Double actualQuantity, Integer id);

    Optional<Incoming> findFirstByActualQuantityGreaterThanAndProductIdOrderByExpirationDateAsc(Double actualQuantity, Integer id);

    @Query(
            "SELECT COALESCE(SUM(incoming.actualQuantity), 0) " +
                    "FROM Incoming incoming " +
                    "WHERE incoming.expirationDate <= :date " +
                    "AND incoming.product.store.id = :storeId"
    )
    Double getTotalNearExpiryItemsByStoreId(Integer storeId, LocalDate date);
}
