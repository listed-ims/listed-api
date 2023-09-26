package com.citu.listed.user;

import com.citu.listed.membership.Membership;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String username;

    @Column
    private String password;
    
    @Column
    private Integer currentStoreId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Membership> memberships;

    @Builder
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.currentStoreId = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Membership membership : this.getMemberships()) {
            for (SimpleGrantedAuthority authority: membership.getAuthorities()) {
                authorities.add(
                        new SimpleGrantedAuthority(membership.getStore().getId()+"_"+authority.getAuthority())
                );
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}