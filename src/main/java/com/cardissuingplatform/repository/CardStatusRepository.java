package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardStatusRepository extends JpaRepository<CardStatus,Long> {
}
