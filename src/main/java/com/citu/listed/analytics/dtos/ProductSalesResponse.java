package com.citu.listed.analytics.dtos;

import com.citu.listed.product.Product;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesResponse {
    private Product product;
    private Double totalSales;
    private Double totalUnitSold;
}
