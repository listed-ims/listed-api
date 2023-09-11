package com.citu.listed.incoming;

import com.citu.listed.product.ProductResponseMapper;
import com.citu.listed.user.UserResponseMapper;
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
