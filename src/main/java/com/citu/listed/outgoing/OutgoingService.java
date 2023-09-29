package com.citu.listed.outgoing;

import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;

public interface OutgoingService {
    OutgoingResponse outProducts(String token, OutgoingRequest request);
    OutgoingResponse getOutgoingTransaction(Integer id);
}
