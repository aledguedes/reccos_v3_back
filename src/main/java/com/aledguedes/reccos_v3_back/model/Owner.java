package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.OWNER;

    @Column(nullable = false)
    private boolean emailVerified;
}