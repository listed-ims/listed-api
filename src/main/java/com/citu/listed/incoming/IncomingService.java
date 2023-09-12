package com.citu.listed.incoming;



public interface IncomingService {

    IncomingResponse inProduct(String token, Integer productId, IncomingRequest request);
    IncomingResponse getIncomingTransaction(Integer id);


}
