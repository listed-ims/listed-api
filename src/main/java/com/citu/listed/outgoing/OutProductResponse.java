package com.citu.listed.outgoing;

import com.citu.listed.product.ProductResponse;

public record OutProductResponse(
        Integer id,
        ProductResponse product,
        Double quantity,
        Double price
) {
}
