package com.citu.listed.outgoing;

import com.citu.listed.product.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutProductRequest {

    @NotNull(message = "Product is required.")
    private Product product;

    private Double quantity;

    private Double price;
}
