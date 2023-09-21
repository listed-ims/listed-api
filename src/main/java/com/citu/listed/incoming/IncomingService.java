package com.citu.listed.incoming;

import com.citu.listed.incoming.dtos.IncomingRequest;
import com.citu.listed.incoming.dtos.IncomingResponse;

import java.util.List;

public interface IncomingService {

    IncomingResponse inProduct(String token, Integer productId, IncomingRequest request);
    List<IncomingResponse> getIncomingTransactions(Integer storeId);
    IncomingResponse getIncomingTransaction(Integer id);

}
