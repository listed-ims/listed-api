package com.citu.listed.outgoing;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, Integer> {

    @Query(
            "SELECT outgoing " +
                    "FROM Outgoing outgoing " +
                    "JOIN outgoing.products outProduct " +
                    "WHERE outProduct.product.store = :store " +
                    "AND (:startDate IS NULL OR DATE(outgoing.transactionDate) >= :startDate) " +
                    "AND (:endDate IS NULL OR DATE(outgoing.transactionDate) <= :endDate) " +
                    "AND (:productId IS NULL OR outProduct.product.id = :productId) " +
                    "AND (:userId IS NULL OR outgoing.user.id = :userId) " +
                    "AND (:category IS NULL OR outgoing.category = :category)"
    )
    List<Outgoing> getByStoreId(
            Store store,
            Integer userId,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            OutgoingCategory category,
            Pageable pageable
    );

    @Query(
            "SELECT COUNT(outgoing) " +
                    "FROM Outgoing outgoing " +
                    "WHERE DATE(outgoing.transactionDate) = DATE(:transactionDate)"
    )
    Long countByTransactionDate(LocalDateTime transactionDate);

    @Query(
            "SELECT COALESCE(SUM(outgoing.revenue), 0) " +
                    "FROM Outgoing outgoing " +
                    "WHERE DATE(outgoing.transactionDate) = :today " +
                    "AND outgoing.id IN " +
                    "(SELECT DISTINCT o.id " +
                            "FROM Outgoing o " +
                            "JOIN o.products outProduct " +
                            "WHERE outProduct.product.store.id = :storeId " +
                            "AND o.category = :salesCategory)"
    )
    Double getTotalRevenueByStoreId(LocalDate today, Integer storeId, OutgoingCategory salesCategory);

    @Query(
            "SELECT COALESCE(SUM(outProduct.quantity), 0) " +
                    "FROM Outgoing outgoing " +
                    "JOIN outgoing.products outProduct " +
                    "WHERE DATE(outgoing.transactionDate) = :today " +
                    "AND outProduct.product.store.id = :storeId " +
                    "AND outgoing.category = :salesCategory"
    )
    Double getTotalItemsSoldToday(LocalDate today, Integer storeId, OutgoingCategory salesCategory);

    @Query(
            "SELECT COALESCE(SUM(outProduct.quantity), 0) " +
                    "FROM Outgoing outgoing " +
                    "JOIN outgoing.products outProduct " +
                    "WHERE outProduct.product.id = :productId"
    )
    Double getTotalOutByProductId(Integer productId);
}
