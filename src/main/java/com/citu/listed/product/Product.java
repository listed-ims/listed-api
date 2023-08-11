package com.citu.listed.product;

import com.citu.listed.store.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String barcode;

    @Column
    private String variant;

    @Column
    private Double salePrice;

    @Column
    private Integer threshold;

    @Column
    private Boolean deleted;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductUnit unit;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}
