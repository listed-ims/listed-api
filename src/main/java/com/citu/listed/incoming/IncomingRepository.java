package com.citu.listed.incoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {




    List<Incoming> findAllByProduct_Store_Id(Integer id);








}
