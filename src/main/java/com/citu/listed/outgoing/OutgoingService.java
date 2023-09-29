package com.citu.listed.outgoing;

import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;

import java.util.List;

public interface OutgoingService {
    OutgoingResponse outProducts(String token, OutgoingRequest request);
    List<OutgoingResponse> getOutgoingTransactions(Integer storeId);
    OutgoingResponse getOutgoingTransaction(Integer id);
}
