package com.citu.listed.incoming.mappers;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.incoming.dtos.IncomingResponse;
import com.citu.listed.product.mappers.ProductResponseMapper;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class IncomingResponseMapper implements Function<Incoming, IncomingResponse> {

    private final ProductResponseMapper productResponseMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public IncomingResponse apply(Incoming incoming) {
        return new IncomingResponse(
                incoming.getId(),
                incoming.getInitialQuantity(),
                incoming.getPurchasePrice(),
                incoming.getExpirationDate(),
                incoming.getComment(),
                incoming.getTransactionDate(),
                incoming.getReferenceNumber(),
                productResponseMapper.apply(incoming.getProduct()),
                userResponseMapper.apply(incoming.getUser())
        );
    }
}
