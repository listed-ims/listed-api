package com.citu.listed.membership;

import com.citu.listed.store.Store;
import com.citu.listed.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @NotNull(message = "Store is required.")
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @NotNull(message = "User is required.")
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Membership(Store store, User user) {
        this.store = store;
        this.user = user;
    }
}
