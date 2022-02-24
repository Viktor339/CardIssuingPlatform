package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.model.Role;
import com.cardissuingplatform.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long id);

    User getUserById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByCompanyAndRole(Company company, Role role, Pageable pageable);
}
