package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
