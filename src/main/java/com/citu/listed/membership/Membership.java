package com.citu.listed.membership;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.permission.Permission;
import com.citu.listed.store.Store;
import com.citu.listed.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column
    @NotNull(message = "Membership status is required.")
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "membership_permissions",
            joinColumns = @JoinColumn(name = "membership_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @Builder
    public Membership(Store store, User user,
                      MembershipStatus membershipStatus,
                      Set<Permission> permissions){
        this.store = store;
        this.user = user;
        this.membershipStatus = membershipStatus;
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions()
                .stream()
                .map(permission ->
                        new SimpleGrantedAuthority(permission.getUserPermission().name())
                )
                .toList();
    }
}
