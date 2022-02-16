package com.cardissuingplatform.unit;

import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.service.UserProvider;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProviderTest {

    @InjectMocks
    private UserProvider userProvider;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .isEnabled(true)
                .id(1L)
                .build();
    }

    @Test
    void checkEnabled() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.ofNullable(user));

        assertEquals(true, userProvider.checkEnabled("1"));

        verify(userRepository, times(1)).findUserById(argThat(id -> id.equals(1L)));
    }

    @Test
    void checkEnabledShouldThrowUserNotFoundException() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userProvider.checkEnabled("1"));

        verify(userRepository, times(1)).findUserById(argThat(id -> id.equals(1L)));
    }

}