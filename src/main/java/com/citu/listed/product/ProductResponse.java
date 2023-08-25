package com.citu.listed.product;

public record ProductResponse(
        Integer id,
        String name,
        String barcode,
        String variant,
        Double salePrice,
        Double threshold,
        ProductUnit unit,
        Double totalQuantity,
        Double totalIn,
        Double totalOut
) {
}
