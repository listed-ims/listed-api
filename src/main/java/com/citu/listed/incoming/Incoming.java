package com.citu.listed.incoming;

import com.citu.listed.product.Product;
import com.citu.listed.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "in_transactions")
@Getter
@Setter
@NoArgsConstructor
public class Incoming {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @NotNull(message = "Product is required.")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonIgnore
    @NotNull(message = "User is required.")
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Double initialQuantity;

    @Column
    private Double actualQuantity;

    @Column
    @NotNull(message = "Purchase price is required.")
    @PositiveOrZero(message = "Purchase price must be greater than or equal to 0.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double purchasePrice;

    @Column
    private LocalDate expirationDate;

    @Column
    private String comment;

    @Column
    private LocalDateTime transactionDate;

    @Column
    private String referenceNumber;

    @Builder
    public Incoming(Integer id, Product product, User user, Double initialQuantity,
                    Double actualQuantity, Double purchasePrice, LocalDate expirationDate, String comment,
                    LocalDateTime transactionDate, String referenceNumber) {

        this.id = id;
        this.product = product;
        this.user = user;
        this.initialQuantity = initialQuantity;
        this.actualQuantity = actualQuantity;
        this.purchasePrice = purchasePrice;
        this.expirationDate = expirationDate;
        this.comment = comment;
        this.transactionDate = transactionDate;
        this.referenceNumber = referenceNumber;
    }
}
