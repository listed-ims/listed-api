package com.citu.listed.product;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductResponseMapper implements Function<Product, ProductResponse> {

    @Override
    public ProductResponse apply(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBarcode(),
                product.getVariant(),
                product.getSalePrice(),
                product.getThreshold(),
                product.getUnit(),
                0,
                0,
                0
        );
    }

}
