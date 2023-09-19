package com.citu.listed.incoming;


import java.util.List;

public interface IncomingService {

    IncomingResponse inProduct(String token, Integer productId, IncomingRequest request);
    List<IncomingResponse> getIncomingTransactions(Integer storeId);
    IncomingResponse getIncomingTransaction(Integer id);

}
