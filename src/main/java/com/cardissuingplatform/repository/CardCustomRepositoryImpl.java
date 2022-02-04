package com.cardissuingplatform.repository;

import com.cardissuingplatform.controller.dto.GetCardDto;
import com.cardissuingplatform.controller.response.GetCardResponse;
import com.cardissuingplatform.model.Card;
import com.cardissuingplatform.model.CardStatus;
import com.cardissuingplatform.model.CardStatus_;
import com.cardissuingplatform.model.Card_;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.model.User_;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CardCustomRepositoryImpl implements CardCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<GetCardResponse> findByGetCardDto(GetCardDto getCardDto) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(GetCardResponse.class);

        var cardStatus = criteria.from(CardStatus.class);
        Join<CardStatus, Card> cards = cardStatus.join(CardStatus_.CARD);
        Join<Card, User> users = cards.join(Card_.OWNED_BY);


        List<Predicate> predicates = new ArrayList<>();
        if (getCardDto.getCurrency() != null) {
            predicates.add(cb.equal(cards.get(Card_.CURRENCY), getCardDto.getCurrency()));
        }
        if (getCardDto.getStatus() != null) {
            predicates.add(cb.equal(cardStatus.get(CardStatus_.STATUS), getCardDto.getStatus()));
        }
        if (getCardDto.getActive() != null) {
            predicates.add(cb.equal(cards.get(Card_.IS_ACTIVE), getCardDto.getActive()));
        }

        List<Order> orders = new ArrayList<>();
        if (getCardDto.getSortLastName() != null) {
            if (getCardDto.getSortLastNameBy().equals("desc")) {
                orders.add(cb.desc(cards.get(Card_.lastName)));
            }
            orders.add(cb.asc(cards.get(Card_.lastName)));
        }
        if (getCardDto.getSortName() != null) {
            if (getCardDto.getSortNameBy().equals("desc")) {
                orders.add(cb.desc(cards.get(Card_.FIRST_NAME)));
            }
            orders.add(cb.asc(cards.get(Card_.FIRST_NAME)));
        }

        criteria.select(cb.construct(GetCardResponse.class,
                        cards.get(Card_.ID),
                        cards.get(Card_.TYPE),
                        cards.get(Card_.VALID_TILL),
                        users.get(User_.FIRST_NAME),
                        cards.get(Card_.LAST_NAME),
                        cards.get(Card_.IS_ACTIVE),
                        cardStatus.get(CardStatus_.STATUS),
                        cards.get(Card_.CURRENCY)))
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(orders);

        return entityManager.createQuery(criteria)
                .setMaxResults(getCardDto.getSize())
                .setFirstResult(getCardDto.getSize() * getCardDto.getPage())
                .getResultList();
    }
}
