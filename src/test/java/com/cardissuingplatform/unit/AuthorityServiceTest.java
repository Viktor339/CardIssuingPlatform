package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.TokenDto;
import com.cardissuingplatform.controller.request.ChangeAuthorityRequest;
import com.cardissuingplatform.controller.response.ChangeAuthorityResponse;
import com.cardissuingplatform.controller.response.GetAuthorityResponse;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.model.Role;
import com.cardissuingplatform.model.User;
import com.cardissuingplatform.repository.AuthorityRepository;
import com.cardissuingplatform.repository.RoleRepository;
import com.cardissuingplatform.repository.UserRepository;
import com.cardissuingplatform.service.AuthorityService;
import com.cardissuingplatform.service.ConverterService;
import com.cardissuingplatform.service.exception.RoleNotFoundException;
import com.cardissuingplatform.service.exception.UserNotBelongToTheCompanyException;
import com.cardissuingplatform.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    private static final Integer SIZE = 1;
    private static final Integer PAGE = 0;
    private static final String ROLE_USER = "ROLE_USER";


    @InjectMocks
    private AuthorityService authorityService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ConverterService converterService;

    private ChangeAuthorityRequest changeAuthorityRequest;
    private ChangeAuthorityResponse changeAuthorityResponse;
    private User accountant;
    private User user;
    private Company company;
    private Role role;
    private List<User> users;
    private List<GetAuthorityResponse> getAuthorityResponses;
    private GetAuthorityResponse getAuthorityResponse;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        changeAuthorityRequest = new ChangeAuthorityRequest();
        changeAuthorityRequest.setUserId(2L);
        changeAuthorityRequest.setAuthorities(List.of(ChangeAuthorityRequest.Authority.READ_CARD_HISTORY, ChangeAuthorityRequest.Authority.READ_CORPORATE_CARDS));

        company = Company.builder()
                .id(1L)
                .build();

        accountant = User.builder()
                .id(2L)
                .company(company)
                .build();

        user = User.builder()
                .id(1L)
                .company(company)
                .build();

        List<String> authorities = List.of("READ_CARD_HISTORY", "READ_CORPORATE_CARDS");

        changeAuthorityResponse = ChangeAuthorityResponse.builder()
                .userId(user.getId())
                .authorities(authorities)
                .build();
        role = Role.builder()
                .roleName(ROLE_USER)
                .build();

        users = List.of(user);

        getAuthorityResponse = GetAuthorityResponse.builder()
                .userId(user.getId())
                .authorities(authorities)
                .build();

        getAuthorityResponses = List.of(getAuthorityResponse);


        tokenDto = TokenDto.builder()
                .userId("1")
                .build();
    }

    @Test
    void change() {

        when(userRepository.getUserById(any(Long.class))).thenReturn(accountant);
        when(userRepository.findUserById(changeAuthorityRequest.getUserId())).thenReturn(Optional.of(user));

        assertEquals(changeAuthorityResponse, authorityService.change(changeAuthorityRequest, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(id -> id == 1));
        verify(userRepository, times(1)).findUserById(argThat(id -> id == 2));
        verify(authorityRepository, times(1)).deleteAllByUser(argThat(user -> user.equals(this.user)));
        verify(authorityRepository, times(2)).save(argThat(authority -> authority.getUser().equals(user)));

    }


    @Test
    void changeShouldThrowUserNotFoundExceptionByAdmin() {

        when(userRepository.getUserById(any(Long.class))).thenReturn(accountant);
        when(userRepository.findUserById(changeAuthorityRequest.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authorityService.change(changeAuthorityRequest, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(id -> id == 1));
        verify(userRepository, times(1)).findUserById(argThat(id -> id == 2));

    }


    @Test
    void changeShouldThrowUserNotBelongToTheCompanyException() {

        accountant.setCompany(Company.builder()
                .id(2L)
                .build());

        when(userRepository.getUserById(any(Long.class))).thenReturn(accountant);
        when(userRepository.findUserById(changeAuthorityRequest.getUserId())).thenReturn(Optional.of(user));

        assertThrows(UserNotBelongToTheCompanyException.class, () -> authorityService.change(changeAuthorityRequest, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(id -> id == 1));
        verify(userRepository, times(1)).findUserById(argThat(id -> id == 2));

    }


    @Test
    void get() {

        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.of(role));
        when(userRepository.findAllByCompanyAndRole(any(Company.class), any(Role.class), any(Pageable.class))).thenReturn(users);
        when(converterService.userToGetAuthorityResponse(any(User.class))).thenReturn(getAuthorityResponse);
        when(userRepository.getUserById(any(Long.class))).thenReturn(user);


        assertEquals(getAuthorityResponses, authorityService.get(SIZE, PAGE, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(id -> id == 1));
        verify(roleRepository, times(1)).findByRoleName(argThat(role -> role.equals(ROLE_USER)));
        verify(userRepository, times(1)).findAllByCompanyAndRole(argThat(company -> company.equals(this.company)),
                argThat(role -> role.equals(this.role)),
                any(Pageable.class)
        );
        verify(converterService, times(1)).userToGetAuthorityResponse(argThat(user -> user.getId() == 1));

    }


    @Test
    void getShouldThrowRoleNotFoundException() {

        when(userRepository.getUserById(any(Long.class))).thenReturn(accountant);
        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> authorityService.get(SIZE, PAGE, tokenDto));

        verify(userRepository, times(1)).getUserById(argThat(id -> id == 1));
        verify(roleRepository, times(1)).findByRoleName(argThat(role -> role.equals(ROLE_USER)));

    }
}