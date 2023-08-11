package com.citu.listed.store;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

}
