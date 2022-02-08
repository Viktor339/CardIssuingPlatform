package com.cardissuingplatform.repository.specification;

import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.CardStatus_;
import com.cardissuingplatform.model.Card_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;


public class CardSpecification {

    public static Specification<CardStatus> withCurrency(String currency) {
        if (currency == null) {
            return null;
        }
        return (root, query, cb) -> {
            Path<Card> card = root.get(CardStatus_.CARD);
            return cb.equal(card.get(Card_.CURRENCY), currency);
        };
    }

    public static Specification<CardStatus> withActive(Boolean isActive) {
        if (isActive == null) {
            return null;
        }
        return (root, query, cb) -> {
            Path<Card> card = root.get(CardStatus_.CARD);
            return cb.equal(card.get(Card_.IS_ACTIVE), isActive);
        };
    }

    public static Specification<CardStatus> withCardStatus(String status) {
        if (status == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get(CardStatus_.STATUS), status);
        }
    }


}
