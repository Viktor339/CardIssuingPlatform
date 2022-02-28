package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CardStatusRepository extends JpaRepository<CardStatus, Long>, JpaSpecificationExecutor<CardStatus> {

    List<CardStatus> findAllCardStatusByCardId(Long id);
}
