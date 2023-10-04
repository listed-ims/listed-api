package com.citu.listed.incoming;

import com.citu.listed.incoming.dtos.IncomingRequest;
import com.citu.listed.incoming.dtos.IncomingResponse;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface IncomingService {

    IncomingResponse inProduct(String token, Integer productId, IncomingRequest request);
    List<IncomingResponse> getIncomingTransactions(
            Integer storeId,
            Integer userId,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            int pageNumber,
            int pageSize,
            Sort.Direction sortOrder
    );
    IncomingResponse getIncomingTransaction(Integer id);

}
