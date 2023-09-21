package com.citu.listed.outgoing;

import com.citu.listed.user.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OutgoingResponse(
        Integer id,
        UserResponse user,
        List<OutProduct> products,
        OutgoingCategory category,
        Double price,
        String comment,
        LocalDateTime transactionDate
) {
}
