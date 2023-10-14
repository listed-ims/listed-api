package com.citu.listed.store;

import com.citu.listed.membership.Membership;
import com.citu.listed.store.enums.StoreStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
public class Store implements Serializable{

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

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Membership> members;

    @Builder
    public Store(String name, StoreStatus status) {
        this.name = name;
        this.status = status;
    }

}
