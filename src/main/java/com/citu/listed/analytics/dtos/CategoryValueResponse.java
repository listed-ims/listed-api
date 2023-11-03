package com.citu.listed.analytics.dtos;

import com.citu.listed.outgoing.enums.OutgoingCategory;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryValueResponse {
    private OutgoingCategory category;
    private Double value;
}
