package com.citu.listed.analytics.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingValueResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    List<CategoryValueResponse> categories;
}
