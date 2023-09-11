package com.citu.listed.incoming;

import com.citu.listed.product.Product;
import com.citu.listed.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

//   List<Incoming> findAllByProductStoreId(Integer storeId);


    List<Incoming> findAllByProduct_Store_Id(Integer id);








}
