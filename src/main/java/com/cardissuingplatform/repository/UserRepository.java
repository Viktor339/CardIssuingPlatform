package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
