package com.citu.listed.analytics.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopProductResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ProductSalesResponse> products;
    private int totalPages;
}
