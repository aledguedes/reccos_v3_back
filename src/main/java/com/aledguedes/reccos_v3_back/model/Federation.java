package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "federations")
@Data
@EqualsAndHashCode(callSuper = true)
public class Federation extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "federation")
    private List<User> users;
}