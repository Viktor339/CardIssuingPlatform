package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CardStatusRepository extends JpaRepository<CardStatus, Long>, JpaSpecificationExecutor<CardStatus> {

}
