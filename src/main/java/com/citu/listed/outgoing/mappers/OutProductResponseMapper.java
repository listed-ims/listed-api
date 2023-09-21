package com.citu.listed.outgoing.mappers;

import com.citu.listed.outgoing.OutProduct;
import com.citu.listed.outgoing.dtos.OutProductResponse;
import com.citu.listed.product.mappers.ProductResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OutProductResponseMapper implements Function<OutProduct, OutProductResponse> {

    private final ProductResponseMapper productResponseMapper;

    @Override
    public OutProductResponse apply(OutProduct outProduct) {
        return new OutProductResponse(
                outProduct.getId(),
                productResponseMapper.apply(outProduct.getProduct()),
                outProduct.getQuantity(),
                outProduct.getPrice()
        );
    }
}
