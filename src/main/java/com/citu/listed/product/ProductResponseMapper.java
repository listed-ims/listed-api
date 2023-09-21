package com.citu.listed.product;

import com.citu.listed.incoming.IncomingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductResponseMapper implements Function<Product, ProductResponse> {

    private final IncomingRepository incomingRepository;

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
                incomingRepository.getTotalQuantityByProductId(product.getId()),
                incomingRepository.getTotalInByProductId(product.getId()),
                0.0
        );
    }

}
