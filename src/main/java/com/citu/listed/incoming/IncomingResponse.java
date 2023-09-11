package com.citu.listed.incoming;

import com.citu.listed.product.ProductResponse;
import com.citu.listed.user.UserResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record IncomingResponse(
        Integer id,
        Double initialQuantity,
        Double purchasePrice,
        LocalDate expirationDate,
        String comment,
        LocalDateTime transactionDate,
        String referenceNumber,
        ProductResponse product,
        UserResponse user
) {
}
