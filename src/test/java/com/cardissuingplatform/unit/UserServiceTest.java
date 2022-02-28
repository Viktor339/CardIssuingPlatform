package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.TokenDto;
import com.cardissuingplatform.controller.request.ChangePasswordRequest;
import com.cardissuingplatform.controller.request.LoginRequest;
import com.cardissuingplatform.controller.request.RegistrationRequest;
import com.cardissuingplatform.controller.response.ChangePasswordResponse;
import com.cardissuingplatform.controller.response.LoginResponse;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.model.Role;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.CompanyRepository;
import com.cardissuingplatform.repository.RoleRepository;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.security.JwtTokenProvider;
import com.cardissuingplatform.service.UserService;
import com.cardissuingplatform.service.exception.AuthenticationException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import com.cardissuingplatform.service.exception.RoleNotFoundException;
import com.cardissuingplatform.service.exception.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String ACCESS = "access";

    private static final String REFRESH = "refresh";
    private static final String PASSWORD = "password";
    private static final String NEW_PASSWORD = "newPassword";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String DATE = "1999-12-12";


    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CompanyRepository companyRepository;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private ChangePasswordRequest changePasswordRequest;
    private ChangePasswordResponse changePasswordResponse;
    private RegistrationRequest registrationRequest;
    private User user;
    private Company company;
    private Role role;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {

        loginRequest = new LoginRequest();
        loginRequest.setEmail(EMAIL);
        loginRequest.setPassword(PASSWORD);


        loginResponse = LoginResponse.builder()
                .id(1L)
                .accessToken(ACCESS)
                .refreshToken(REFRESH)
                .build();

        registrationRequest = RegistrationRequest.builder()
                .roleId(1L)
                .companyId(1L)
                .username(USERNAME)
                .password(PASSWORD)
                .dateOfBirth(LocalDate.parse(DATE))
                .build();

        role = Role.builder()
                .build();

        company = Company.builder()
                .id(1L)
                .build();

        user = User.builder()
                .id(1L)
                .password(PASSWORD)
                .dateOfBirth(Instant.parse("1999-12-11T21:00:00Z"))
                .createdAt(Instant.parse("1999-12-11T21:00:00Z"))
                .role(role)
                .company(company)
                .build();

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword(NEW_PASSWORD);
        changePasswordRequest.setOldPassword(PASSWORD);

        changePasswordResponse = ChangePasswordResponse.builder()
                .userId(user.getId())
                .build();
        tokenDto = TokenDto.builder()
                .userId(1L)
                .build();
    }

    @Test
    void login() {

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(user)).thenReturn(ACCESS);
        when(jwtTokenProvider.createRefreshToken()).thenReturn(REFRESH);

        assertEquals(loginResponse, userService.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(argThat(email -> email.equals(EMAIL)));
        verify(passwordEncoder, times(1)).matches(argThat(pass -> pass.equals(PASSWORD)),
                argThat(userPass -> userPass.equals(PASSWORD)));
        verify(jwtTokenProvider, times(1)).createAccessToken(argThat(user -> user.getId() == 1L));
        verify(jwtTokenProvider, times(1)).createRefreshToken();

    }

    @Test
    void loginShouldThrowAuthenticationException() {

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(argThat(email -> email.equals(EMAIL)));

    }

    @Test
    void loginShouldThrowAuthenticationExceptionFromPasswordEncoder() {

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class, () ->
                userService.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(argThat(email -> email.equals(EMAIL)));
        verify(passwordEncoder, times(1)).matches(argThat(pass -> pass.equals(PASSWORD)),
                argThat(userPass -> userPass.equals(PASSWORD)));

    }

    @Test
    void registration() {

        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findById(registrationRequest.getRoleId())).thenReturn(Optional.ofNullable(role));
        when(companyRepository.findById(registrationRequest.getCompanyId())).thenReturn(Optional.ofNullable(company));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(user.getId(), userService.registration(registrationRequest));

        verify(userRepository, times(1)).findByUsername(argThat(userName -> userName.equals(USERNAME)));
        verify(roleRepository, times(1)).findById(argThat(roleId -> roleId.equals(1L)));
        verify(companyRepository, times(1)).findById(argThat(companyId -> company.getId() == 1L));
        verify(userRepository, times(1)).save(argThat(user -> !user.isEnabled()));
        verify(passwordEncoder, times(1)).encode(argThat(pass -> pass.equals(PASSWORD)));

    }

    @Test
    void registrationShouldThrowUserAlreadyExistException() {

        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(Optional.ofNullable(user));

        assertThrows(UserAlreadyExistException.class, () -> userService.registration(registrationRequest));

        verify(userRepository, times(1)).findByUsername(argThat(userName -> userName.equals(USERNAME)));

    }

    @Test
    void registrationShouldThrowRoleNotFoundException() {

        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findById(registrationRequest.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.registration(registrationRequest));

        verify(userRepository, times(1)).findByUsername(argThat(userName -> userName.equals(USERNAME)));
        verify(roleRepository, times(1)).findById(argThat(roleId -> roleId.equals(1L)));

    }

    @Test
    void registrationShouldThrowCompanyNotFoundException() {

        when(userRepository.findByUsername(registrationRequest.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findById(registrationRequest.getRoleId())).thenReturn(Optional.empty());
        when(roleRepository.findById(registrationRequest.getRoleId())).thenReturn(Optional.ofNullable(role));

        assertThrows(CompanyNotFoundException.class, () -> userService.registration(registrationRequest));

        verify(userRepository, times(1)).findByUsername(argThat(userName -> userName.equals(USERNAME)));
        verify(roleRepository, times(1)).findById(argThat(roleId -> roleId.equals(1L)));
        verify(companyRepository, times(1)).findById(argThat(companyId -> company.getId() == 1L));

    }


    @Test
    void changePassword() {

        when(userRepository.getUserById(any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())).thenReturn(true);

        assertEquals(changePasswordResponse, userService.changePassword(changePasswordRequest, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(userId -> userId.equals(1L)));
        verify(passwordEncoder, times(1)).matches(argThat(pass -> pass.equals(PASSWORD)),
                argThat(userPass -> userPass.equals(PASSWORD)));
        verify(passwordEncoder, times(1)).encode(changePasswordRequest.getNewPassword());

    }


    @Test
    void changePasswordShouldThrowAuthenticationException() {

        when(userRepository.getUserById(any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> userService.changePassword(changePasswordRequest, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(userId -> userId.equals(1L)));
        verify(passwordEncoder, times(1)).matches(argThat(pass -> pass.equals(PASSWORD)),
                argThat(userPass -> userPass.equals(PASSWORD)));
    }
}