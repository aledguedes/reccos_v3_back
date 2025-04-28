package com.aledguedes.reccos_v3_back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

    @Column
    private String street;

    @Column
    private String number;

    @Column
    private String complement;

    @Column
    private String neighborhood;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String zipCode;
}