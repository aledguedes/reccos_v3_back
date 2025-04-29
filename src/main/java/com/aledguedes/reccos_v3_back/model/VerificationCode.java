package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Getter
@Setter
public class VerificationCode extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;
}