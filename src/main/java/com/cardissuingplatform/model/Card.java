package com.cardissuingplatform.model;

import liquibase.pro.packaged.I;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "valid_till")
    private Timestamp validTill;

    private String number;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String currency;

    @OneToOne
    private Company company;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
//    @Column(name = "owned_by")
    private User ownedBy;


    public enum Type {
        PERSONAL, CORPORATE
    }


    //id (bigserial) not null
    //
    //type (enum: personal/corporate) not null
    //
    //valid_till (date) not null
    //
    //number (varchar 4) not null - последние 4 цифры карты
    //
    //first_name (varchar 255) not null - имя владельца. Может быть отличное от owned_by
    //
    //last_name (varchar 255) not null - фамилия владельца. Может быть отличное от owned_by
    //
    //currency (varchar 3) not null - код валюты (обычно выглядит как 3-значное число)
    //
    //company_id (bigint) not null ← foreign_key на таблицу company
    //
    //created_by (bigint) not null
    //
    //is_active - (boolean) not null
    //
    //owned_by (bigint) ← foreign_key на таблицу user
}
