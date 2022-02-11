package com.cardissuingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(Authority.AuthorityId.class)
@Builder(toBuilder = true)
public class Authority {

    @Id
    @OneToOne
    private User user;

    @Id
    @Column(name = "authority_name")
    private AuthorityEnum authorityName;

    @Column(name = "created_time")
    private Instant createdTime;

    @OneToOne
    private User createdBy;

    public enum AuthorityEnum {
        READ_INTERMEDIATE_CARD_STATUS, READ_CORPORATE_CARDS, READ_CARD_HISTORY
    }

    @Data
    @RequiredArgsConstructor
    public static class AuthorityId implements Serializable {
        private Authority.AuthorityEnum authorityName;
        private Long user;
    }

}