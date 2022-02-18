package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.request.ChangeAuthorityRequest;
import com.cardissuingplatform.controller.response.ChangeAuthorityResponse;
import com.cardissuingplatform.controller.response.GetAuthorityResponse;
import com.cardissuingplatform.model.Authority;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.model.Role;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.model.User_;
import com.cardissuingplatform.repository.AuthorityRepository;
import com.cardissuingplatform.repository.RoleRepository;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.security.JwtTokenProvider;
import com.cardissuingplatform.service.exception.RoleNotFoundException;
import com.cardissuingplatform.service.exception.UserNotBelongToTheCompanyException;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorityService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final ConverterService converterService;

    @Transactional
    public ChangeAuthorityResponse change(ChangeAuthorityRequest changeAuthorityRequest,String token) {

        String accountantId = jwtTokenProvider.getUserId(token);

        User accountant = userRepository.getUserById(Long.parseLong(accountantId));
        User user = userRepository.findUserById(changeAuthorityRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!accountant.getCompany().equals(user.getCompany())) {
            throw new UserNotBelongToTheCompanyException("User not belong to the company");
        }

        authorityRepository.deleteAllByUser(user);

        changeAuthorityRequest.getAuthorities().forEach(authority -> authorityRepository.save(
                Authority.builder()
                        .user(user)
                        .authorityName(Authority.AuthorityEnum.valueOf(authority.name()))
                        .createdTime(Instant.now())
                        .createdBy(accountant)
                        .build())
        );


        return ChangeAuthorityResponse.builder()
                .userId(user.getId())
                .authorities(
                        changeAuthorityRequest.getAuthorities().stream()
                                .map(Enum::name)
                                .collect(Collectors.toList()))
                .build();

    }

    @Transactional
    public List<GetAuthorityResponse> get(Integer size, Integer page,String token) {

        String accountantId = jwtTokenProvider.getUserId(token);

        User accountant = userRepository.getUserById(Long.parseLong(accountantId));

        Role role = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(User_.ID).ascending());

        Company company = accountant.getCompany();

        List<User> allUsersByCompany = userRepository.findAllByCompanyAndRole(company, role, pageable);

        return allUsersByCompany.stream()
                .map(converterService::userToGetAuthorityResponse)
                .collect(Collectors.toList());

    }

}
