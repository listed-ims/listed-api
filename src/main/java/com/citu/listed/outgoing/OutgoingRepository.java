package com.citu.listed.outgoing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, Integer> {

    @Query(
            "SELECT COUNT(outgoing) " +
                    "FROM Outgoing outgoing " +
                    "WHERE DATE(outgoing.transactionDate) = DATE(:transactionDate)"
    )
    Long countByTransactionDate(LocalDateTime transactionDate);
}
