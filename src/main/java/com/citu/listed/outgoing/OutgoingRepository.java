package com.citu.listed.outgoing;

import com.citu.listed.outgoing.enums.OutgoingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, Integer> {
    List<Outgoing> findByProductsProductStoreId(Integer storeId);

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
