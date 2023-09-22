package com.citu.listed.outgoing.dtos;

import com.citu.listed.outgoing.OutProduct;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.user.dtos.UserResponse;
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
