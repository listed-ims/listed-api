package com.citu.listed.outgoing.dtos;

import com.citu.listed.outgoing.OutProduct;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingRequest {

    @NotNull(message = "Product is required.")
    private List<OutProduct> products;

    private OutgoingCategory category;

    private Double price;

    private String comment;

    private LocalDateTime transactionDate;
}
