package com.citu.listed.outgoing;

import com.citu.listed.analytics.dtos.CategoryValueResponse;
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
            "SELECT NEW com.citu.listed.analytics.dtos.CategoryValueResponse(outgoing.category, SUM(outgoing.price) - SUM(outgoing.revenue)) " +
                    "FROM Outgoing outgoing " +
                    "INNER JOIN outgoing.products outProduct " +
                    "WHERE outProduct.product.store.id = :storeId " +
                    "AND outgoing.category != 'SALES' " +
                    "AND DATE(outgoing.transactionDate) >= :startDate " +
                    "AND DATE(outgoing.transactionDate) <= :endDate " +
                    "GROUP BY outgoing.category " +
                    "ORDER BY outgoing.category DESC"
    )
    List<CategoryValueResponse> getTotalCategoryValueByStoreId(Integer storeId, LocalDate startDate, LocalDate endDate);

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
            value = "WITH RECURSIVE DateRanges AS " +
                            "(SELECT MIN_DATE AS start_date, DATE_ADD(MIN_DATE, INTERVAL 6 DAY) AS end_date FROM " +
                                    "(SELECT MIN(transaction_date) AS MIN_DATE, CURDATE() AS MAX_DATE " +
                                            "FROM out_transactions " +
                                            "INNER JOIN out_products " +
                                            "ON out_transactions.id = out_products.outgoing_id " +
                                            "INNER JOIN products " +
                                            "ON products.id = out_products.product_id " +
                                            "WHERE products.store_id = :storeId) AS date_range " +
                                            "UNION ALL " +
                                            "SELECT DATE_ADD(end_date, INTERVAL 1 DAY), DATE_ADD(end_date, INTERVAL 7 DAY) " +
                                            "FROM DateRanges " +
                                            "WHERE end_date <= CURDATE()) " +
                    "SELECT YEAR(start_date) AS year, LPAD(WEEK(start_date, 1), 2, '0') AS week " +
                            "FROM DateRanges " +
                            "LEFT JOIN out_transactions ON " +
                            "transaction_date >= start_date AND transaction_date <= end_date " +
                            "GROUP BY year, week " +
                            "ORDER BY year DESC, week DESC",
            nativeQuery = true
    )
    List<Map<String, Object>> getWeeklyDateRange(Integer storeId, Pageable pageable);

    @Query(
            value = "WITH RECURSIVE MonthRanges AS " +
                            "(SELECT MIN_DATE AS start_date, DATE_ADD(MIN_DATE, INTERVAL 1 MONTH) AS end_date FROM " +
                                    "(SELECT MIN(transaction_date) AS MIN_DATE, CURDATE() AS MAX_DATE " +
                                            "FROM out_transactions " +
                                            "INNER JOIN out_products " +
                                            "ON out_transactions.id = out_products.outgoing_id " +
                                            "INNER JOIN products " +
                                            "ON products.id = out_products.product_id " +
                                            "WHERE products.store_id = :storeId) AS date_range " +
                                            "UNION ALL " +
                                            "SELECT DATE_ADD(end_date, INTERVAL 1 DAY), DATE_ADD(DATE_ADD(end_date, INTERVAL 1 MONTH), INTERVAL 1 DAY) " +
                                            "FROM MonthRanges " +
                                            "WHERE YEAR(end_date) * 100 + MONTH(end_date) <= YEAR(MAX_DATE) * 100 + MONTH(MAX_DATE)) " +
                    "SELECT YEAR(start_date) AS year, LPAD(MONTH(start_date), 2, '0') AS month " +
                            "FROM MonthRanges " +
                            "LEFT JOIN out_transactions ON " +
                            "transaction_date >= start_date AND transaction_date < end_date " +
                            "GROUP BY year, month " +
                            "ORDER BY year DESC, month DESC",
            nativeQuery = true
    )
    List<Map<String, Object>> getMonthlyDateRange(Integer storeId, Pageable pageable);
}
