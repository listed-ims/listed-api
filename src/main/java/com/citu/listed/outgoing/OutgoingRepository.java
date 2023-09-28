package com.citu.listed.outgoing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutgoingRepository extends JpaRepository<Outgoing, Integer> {
    List<Outgoing> findByProductsProductStoreId(Integer storeId);
}
