package com.citu.listed.outgoing;

import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface OutgoingService {
    OutgoingResponse outProducts(String token, OutgoingRequest request);
    List<OutgoingResponse> getOutgoingTransactions(
            Integer storeId,
            Integer userId,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            OutgoingCategory category,
            int pageNumber,
            int pageSize,
            Sort.Direction sortOrder
    );
    OutgoingResponse getOutgoingTransaction(Integer id);
}
