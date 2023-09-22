package com.citu.listed.outgoing;

import com.citu.listed.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "out_products")
@Getter
@Setter
@NoArgsConstructor
public class OutProduct {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @NotNull(message = "Product is required.")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Double quantity;

    @Column
    private Double price;

    @Builder
    public OutProduct(Product product, Double quantity, Double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
