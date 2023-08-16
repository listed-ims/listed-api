package com.citu.listed.store;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    @NotBlank(message = "Store name is required.")
    private String name;

    @Column
    @NotNull(message = "Store status is required.")
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder
    public Store(String name, StoreStatus status) {
        this.name = name;
        this.status = status;
    }

}
