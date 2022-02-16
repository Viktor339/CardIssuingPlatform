package com.cardissuingplatform.service;

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
import com.cardissuingplatform.service.exception.AuthenticationException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import com.cardissuingplatform.service.exception.RoleNotFoundException;
import com.cardissuingplatform.service.exception.UserAlreadyExistException;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("Incorrect username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Incorrect username or password");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return LoginResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Transactional
    public Long registration(RegistrationRequest registrationRequest) {

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("User already exist");
        }

        Role role = roleRepository.findById(registrationRequest.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        Company company = companyRepository.findById(registrationRequest.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        User user = userRepository.save(User.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .email(registrationRequest.getEmail())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .dateOfBirth(registrationRequest.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant())
                .createdAt(Instant.now())
                .role(role)
                .company(company)
                .build());

        return user.getId();
    }

    @Transactional
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getPrincipal().toString();

        String userId = jwtTokenProvider.getUserId(token);

        User user = userRepository.findUserById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        return ChangePasswordResponse.builder().userId(user.getId()).build();
    }
}
