package com.citu.listed.permission;

import com.citu.listed.membership.Membership;
import com.citu.listed.permission.enums.UserPermissions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    @Enumerated(EnumType.STRING)
    private UserPermissions userPermission;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Membership> membershipSet = new HashSet<>();

    @Builder
    public Permission(Integer id, UserPermissions userPermission){
        this.id = id;
        this.userPermission = userPermission;
    }
}
