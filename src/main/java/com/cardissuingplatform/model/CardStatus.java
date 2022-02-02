package com.cardissuingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "card_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private Timestamp created;
    @Column(name = "previous_status")
    private String previousStatus;

    @OneToOne
    private Card card;

    //id (bigserial) not null
    //
    //status (varchar 3) not null - статус карточки (обычно выглядит как 3-значное число)
    //
    //created (timestamp) not null
    //
    //previous_status (varchar 3)
    //
    //card_id (bigint) not null oreign_key на таблицу card
}
