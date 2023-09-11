package com.citu.listed.incoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Integer> {

    @Query("SELECT COUNT(incoming) FROM Incoming incoming WHERE DATE(incoming.transactionDate) = DATE(:transactionDate)")
    long countByTransactionDate(LocalDateTime transactionDate);

}
