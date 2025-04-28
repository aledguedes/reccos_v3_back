package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // OWNER, ADMIN, USER, TEAM_MANAGER, ASSISTANT_1, ASSISTANT_2

    @ManyToOne
    @JoinColumn(name = "federation_id")
    private Federation federation;

    @Column
    private boolean emailVerified;

    @Column
    private String emailVerificationCode;

    @Column
    private Long emailVerificationCodeExpiry;

    @Column
    private String fullName;

    @Column
    private String phoneNumber;

    @Column
    private String cpf;

    @Embedded
    private Address address;
}