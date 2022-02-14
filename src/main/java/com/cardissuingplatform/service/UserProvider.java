package com.cardissuingplatform.service;

import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserProvider {

    private final UserRepository userRepository;

    @Cacheable(value = "user", key = "#id")
    public Boolean checkEnabled(String id) {
        User user = userRepository.findUserById(Long.parseLong(id))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.isEnabled();
    }
}
