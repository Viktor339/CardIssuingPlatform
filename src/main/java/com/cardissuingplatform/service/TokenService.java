package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.response.RefreshTokenResponse;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.security.JwtTokenProvider;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public RefreshTokenResponse refresh(String token, Long id) {

        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        jwtTokenProvider.validateToken(token);

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
