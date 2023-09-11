package com.citu.listed.incoming;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record IncomingResponse(
        Integer id,
        Double initialQuantity,
        Double purchasePrice,
        LocalDate expirationDate,
        String comment,
        LocalDateTime transactionDate

) {
}
