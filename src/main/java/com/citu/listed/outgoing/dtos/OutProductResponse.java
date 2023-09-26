package com.citu.listed.outgoing.dtos;

import com.citu.listed.product.dtos.ProductResponse;

public record OutProductResponse(
        Integer id,
        ProductResponse product,
        Double quantity,
        Double price
) {
}
