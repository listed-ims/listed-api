package com.citu.listed.outgoing;

import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, Integer> {

    @Query(
            "SELECT outgoing " +
                    "FROM Outgoing outgoing " +
                    "WHERE outgoing.id IN " +
                    "(SELECT DISTINCT outgoing.id " +
                            "FROM Outgoing outgoing " +
                            "JOIN outgoing.products outProduct " +
                            "WHERE outProduct.product.store = :store " +
                            "AND (:startDate IS NULL OR DATE(outgoing.transactionDate) >= :startDate) " +
                            "AND (:endDate IS NULL OR DATE(outgoing.transactionDate) <= :endDate) " +
                            "AND (:productId IS NULL OR outProduct.product.id = :productId) " +
                            "AND (:#{#userIds == null} = true OR outgoing.user.id IN :userIds) " +
                            "AND (:#{#categories == null} = true OR outgoing.category IN :categories))"
    )
    List<Outgoing> getByStoreId(
            Store store,
            List<Integer> userIds,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            List<OutgoingCategory> categories,
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
                    "WHERE DATE(outgoing.transactionDate) >= :startDate " +
                    "AND DATE(outgoing.transactionDate) <= :endDate " +
                    "AND outgoing.id IN " +
                    "(SELECT DISTINCT o.id " +
                            "FROM Outgoing o " +
                            "JOIN o.products outProduct " +
                            "WHERE outProduct.product.store.id = :storeId " +
                            "AND o.category = 'SALES')"
    )
    Double getTotalRevenueByStoreId(Integer storeId, LocalDate startDate, LocalDate endDate);

    @Query(
            "SELECT COALESCE(SUM(outProduct.quantity), 0) " +
                    "FROM Outgoing outgoing " +
                    "JOIN outgoing.products outProduct " +
                    "WHERE DATE(outgoing.transactionDate) >= :startDate " +
                    "AND DATE(outgoing.transactionDate) <= :endDate " +
                    "AND outProduct.product.store.id = :storeId " +
                    "AND outgoing.category = 'SALES'"
    )
    Double getTotalItemsSoldByStoreId(Integer storeId, LocalDate startDate, LocalDate endDate);

    @Query(
            "SELECT COALESCE(SUM(outProduct.quantity), 0) " +
                    "FROM Outgoing outgoing " +
                    "JOIN outgoing.products outProduct " +
                    "WHERE outProduct.product.id = :productId"
    )
    Double getTotalOutByProductId(Integer productId);

    @Query(
            value = "WITH RECURSIVE DateRange AS " +
                            "(SELECT MIN(transaction_date) AS start_date, MAX(transaction_date) AS end_date " +
                                    "FROM out_transactions " +
                                    "INNER JOIN out_products " +
                                    "ON out_transactions.id = out_products.outgoing_id " +
                                    "INNER JOIN products " +
                                    "ON products.id = out_products.product_id " +
                                    "WHERE products.store_id = :storeId " +
                                    "UNION ALL " +
                                    "SELECT DATE_ADD(start_date, INTERVAL 7 DAY), end_date " +
                                    "FROM DateRange " +
                                    "WHERE DATE_ADD(start_date, INTERVAL 7 DAY) <= end_date) " +
                    "SELECT YEAR(start_date) AS year, WEEK(start_date, 1) AS week " +
                            "FROM DateRange " +
                            "ORDER BY year DESC, week DESC",
            nativeQuery = true
    )
    List<Map<String, Object>> getWeeklyDateRange(Integer storeId, Pageable pageable);

    @Query(
            value = "WITH RECURSIVE DateRange AS " +
                            "(SELECT MIN(transaction_date) AS start_date, MAX(transaction_date) AS end_date " +
                                    "FROM out_transactions " +
                                    "INNER JOIN out_products " +
                                    "ON out_transactions.id = out_products.outgoing_id " +
                                    "INNER JOIN products " +
                                    "ON products.id = out_products.product_id " +
                                    "WHERE products.store_id = :storeId " +
                                    "UNION ALL " +
                                    "SELECT DATE_ADD(LAST_DAY(start_date), INTERVAL 1 DAY), end_date " +
                                    "FROM DateRange " +
                                    "WHERE DATE_ADD(LAST_DAY(start_date), INTERVAL 1 DAY) <= end_date) " +
                    "SELECT YEAR(start_date) AS year, MONTH(start_date) AS month " +
                            "FROM DateRange " +
                            "ORDER BY year DESC, month DESC",
            nativeQuery = true
    )
    List<Map<String, Object>> getMonthlyDateRange(Integer storeId, Pageable pageable);
}
