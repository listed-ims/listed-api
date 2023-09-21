package com.citu.listed.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(
            "SELECT p FROM Product p WHERE " +
                    "(LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%') " +
                    "OR LOWER(p.variant) LIKE CONCAT('%', LOWER(:keyword), '%')) " +
                    "AND p.store.id = :storeId "
    )
    List<Product> findByStoreId(Integer storeId, String keyword, Pageable pageable);
    
    List<Product> findByStoreIdAndBarcode(Integer storeId, String barcode);

    @Query(
            "SELECT product " +
                    "FROM Product product " +
                    "LEFT JOIN Incoming incoming ON incoming.product.id = product.id " +
                    "WHERE product.store.id = :storeId " +
                    "GROUP BY product " +
                    "HAVING product.threshold >= COALESCE(SUM(incoming.actualQuantity), 0) " +
                    "AND COALESCE(SUM(incoming.actualQuantity), 0) != 0"
    )
    List<Product> getLowStockProductsByStoreId(Integer storeId, Pageable pageable);

    @Query(
            "SELECT product " +
                    "FROM Product product " +
                    "LEFT JOIN Incoming incoming ON incoming.product.id = product.id " +
                    "WHERE product.store.id = :storeId " +
                    "GROUP BY product " +
                    "HAVING COALESCE(SUM(incoming.actualQuantity), 0) = 0"
    )
    List<Product> getNoStockProductsByStoreId(Integer storeId, Pageable pageable);
}
