package com.citu.listed.incoming.dtos;

import com.citu.listed.product.dtos.ProductResponse;
import com.citu.listed.user.dtos.UserResponse;

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
