package com.citu.listed.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomingRequest {

    @NotNull(message = "Quantity is required.")
    private Double initialQuantity;

    @NotNull(message = "Purchase price is required.")
    private Double purchasePrice;

    private LocalDate expirationDate;

    private String comment;
}
