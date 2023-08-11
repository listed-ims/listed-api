package com.citu.listed.product;

public record ProductResponse(
        Integer id,
        String name,
        String barcode,
        String variant,
        Double salePrice,
        Integer threshold,
        ProductUnit unit,
        Integer totalQuantity,
        Integer totalIn,
        Integer totalOut
) {
}
