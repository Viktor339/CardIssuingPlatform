package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.response.RefreshTokenResponse;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.security.JwtTokenProvider;
import com.cardissuingplatform.service.TokenService;
import com.cardissuingplatform.service.exception.AuthenticationException;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private static final String TOKEN = "token";


    @InjectMocks
    private TokenService tokenService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserRepository userRepository;

    private User user;
    private RefreshTokenResponse refreshTokenResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();
        refreshTokenResponse= RefreshTokenResponse.builder()
                .accessToken(ACCESS)
                .refreshToken(REFRESH)
                .build();
    }

    @Test
    void refresh() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.ofNullable(user));
        when(jwtTokenProvider.createAccessToken(user)).thenReturn(ACCESS);
        when(jwtTokenProvider.createRefreshToken()).thenReturn(REFRESH);

        assertEquals(refreshTokenResponse, tokenService.refresh(TOKEN,1L));

        verify(jwtTokenProvider,times(1)).validateToken(TOKEN);
        verify(userRepository, times(1)).findUserById(argThat(id -> id == 1L));
        verify(jwtTokenProvider, times(1)).createAccessToken(argThat(user -> user.getId() == 1L));
        verify(jwtTokenProvider, times(1)).createRefreshToken();

    }

    @Test
    void refreshShouldThrowUserNotFoundException() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> tokenService.refresh(TOKEN,1L));

        verify(userRepository, times(1)).findUserById(argThat(id -> id == 1L));

    }
}