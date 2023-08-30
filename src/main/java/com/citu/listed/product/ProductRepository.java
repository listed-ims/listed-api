package com.citu.listed.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.name) LIKE CONCAT('%', LOWER(:filter), '%') " +
            "OR LOWER(p.variant) LIKE CONCAT('%', LOWER(:filter), '%')) " +
            "AND p.store.id = :storeId ")
    List<Product> findByStoreId(Integer storeId, String filter, Pageable pageable);
    List<Product> findByStoreIdAndBarcode(Integer storeId, String barcode);
}
