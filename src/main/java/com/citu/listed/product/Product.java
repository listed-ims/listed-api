package com.citu.listed.product;

import com.citu.listed.product.enums.ProductUnit;
import com.citu.listed.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;

@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    @NotBlank(message = "Product name is required.")
    private String name;

    @Column
    private String barcode;

    @Column
    private String variant;

    @Column
    @NotNull(message = "Sale price is required.")
    @PositiveOrZero(message = "Sale price must be greater than or equal to 0.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double salePrice;

    @Column
    private Double  threshold;

    @Column
    @NotNull(message = "Unit is required.")
    @Enumerated(EnumType.STRING)
    private ProductUnit unit;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @JsonIgnore
    @Column
    private Boolean deleted;

    @Builder
    public Product(String name, String barcode, String variant, Double salePrice,
                   Double threshold, ProductUnit unit, Store store) {
        this.name = name;
        this.barcode = barcode;
        this.variant = variant;
        this.salePrice = salePrice;
        this.threshold = threshold;
        this.unit = unit;
        this.store = store;
        this.deleted = false;
    }

}
