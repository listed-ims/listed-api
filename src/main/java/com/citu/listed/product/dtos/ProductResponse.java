package com.citu.listed.product.dtos;

import com.citu.listed.product.enums.ProductUnit;

public record ProductResponse(
        Integer id,
        String name,
        String barcode,
        String variant,
        Double salePrice,
        Double threshold,
        ProductUnit unit,
        Double quantity,
        Double totalIn,
        Double totalOut
) {
}
