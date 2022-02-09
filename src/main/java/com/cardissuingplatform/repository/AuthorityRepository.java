package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
