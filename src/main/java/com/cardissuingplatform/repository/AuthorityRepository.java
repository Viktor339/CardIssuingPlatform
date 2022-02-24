package com.cardissuingplatform.repository;

import com.cardissuingplatform.model.Authority;
import com.cardissuingplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    List<Authority> findAllByUser(User user);

    void deleteAllByUser(User user);


}
