package com.cardissuingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;
    @Column(name = "valid_till")
    private Instant validTill;
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
    private User ownedBy;

    public enum Type {
        PERSONAL, CORPORATE
    }
}
