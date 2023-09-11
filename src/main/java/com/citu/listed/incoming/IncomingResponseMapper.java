package com.citu.listed.incoming;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IncomingResponseMapper implements Function<Incoming, IncomingResponse> {

    @Override
    public IncomingResponse apply(Incoming incoming) {
        return new IncomingResponse(
                incoming.getId(),
                incoming.getInitialQuantity(),
                incoming.getPurchasePrice(),
                incoming.getExpirationDate(),
                incoming.getComment(),
                incoming.getTransactionDate()
        );
    }
}
