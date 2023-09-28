package com.citu.listed.outgoing;

import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "out_transactions")
@Getter
@Setter
@NoArgsConstructor
public class Outgoing {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "outgoing_id")
    private List<OutProduct> products;

    @Column
    @Enumerated(EnumType.STRING)
    private OutgoingCategory category;

    @Column
    private Double price;

    @Column
    private Double revenue;

    @Column
    private String comment;

    @Column
    private LocalDateTime transactionDate;

    @Column
    private String referenceNumber;

    @Builder
    public Outgoing(User user, List<OutProduct> products, OutgoingCategory category, Double price, Double revenue, String comment, LocalDateTime transactionDate, String referenceNumber) {
        this.user = user;
        this.products = products;
        this.category = category;
        this.price = price;
        this.revenue = revenue;
        this.comment = comment;
        this.transactionDate = transactionDate;
        this.referenceNumber = referenceNumber;
    }
}
