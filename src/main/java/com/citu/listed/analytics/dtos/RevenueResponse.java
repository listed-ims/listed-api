package com.citu.listed.analytics.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Double revenue;
}
