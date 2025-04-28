package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_federations")
public class UserFederation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "federation_id", nullable = false)
    private Federation federation;

    @CreationTimestamp
    @Column(name = "associated_at", nullable = false, updatable = false)
    private LocalDateTime associatedAt;
}