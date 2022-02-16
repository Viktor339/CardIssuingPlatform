package com.cardissuingplatform.unit;

import com.cardissuingplatform.controller.dto.ChangeCompanyResponse;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.controller.response.GetCompanyResponse;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.repository.CompanyRepository;
import com.cardissuingplatform.service.CompanyService;
import com.cardissuingplatform.service.exception.CompanyAlreadyExistsException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyService companyService;

    private CreateCompanyRequest createCompanyRequest;
    private ChangeCompanyRequest changeCompanyRequest;
    private ChangeCompanyResponse changeCompanyResponse;
    private final List<GetCompanyResponse> getCompanyResponseList = new ArrayList<>();
    private Company company;

    @BeforeEach
    void setUp() {
        createCompanyRequest = new CreateCompanyRequest();
        createCompanyRequest.setName("A");
        createCompanyRequest.setIsEnabled(true);

        company = Company.builder()
                .id(1L)
                .name("A")
                .isEnabled(true)
                .build();

        changeCompanyRequest = new ChangeCompanyRequest();
        changeCompanyRequest.setName("A");
        changeCompanyRequest.setIsEnabled(true);

        changeCompanyResponse = ChangeCompanyResponse.builder()
                .id(1L)
                .name("A")
                .isEnabled(true)
                .build();

        GetCompanyResponse getCompanyResponse = GetCompanyResponse.builder()
                .id(1L)
                .isEnabled(false)
                .name("B").build();

        getCompanyResponseList.add(getCompanyResponse);

    }

    @Test
    void create() {

        when(companyRepository.findCompanyByName(createCompanyRequest.getName())).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            Company returnedCompany = invocation.getArgument(0);
            returnedCompany.setId(1L);
            assertEquals(company, returnedCompany);
            return null;
        }).when(companyRepository).save(any(Company.class));


        companyService.create(createCompanyRequest);

        verify(companyRepository, times(1)).findCompanyByName(Mockito.argThat(create -> createCompanyRequest.getName().equals("A")));
        verify(companyRepository, times(1)).save(Mockito.argThat(company -> company.getId() == 1L));

    }


    @Test
    void createShouldThrowCompanyAlreadyExistsException() {

        when(companyRepository.findCompanyByName(createCompanyRequest.getName())).thenReturn(Optional.ofNullable(company));

        assertThrows(CompanyAlreadyExistsException.class, () -> companyService.create(createCompanyRequest));

        verify(companyRepository, times(1)).findCompanyByName(Mockito.argThat(create -> createCompanyRequest.getName().equals("A")));

    }


    @Test
    void change() {
        when(companyRepository.findCompanyByName(createCompanyRequest.getName())).thenReturn(Optional.ofNullable(company));

        assertEquals(changeCompanyResponse, companyService.change(changeCompanyRequest));

        verify(companyRepository, times(1)).findCompanyByName(Mockito.argThat(create -> createCompanyRequest.getName().equals("A")));

    }

    @Test
    void changeShouldThrowCompanyNotFoundException() {
        when(companyRepository.findCompanyByName(createCompanyRequest.getName())).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () ->
                companyService.change(changeCompanyRequest));

        verify(companyRepository, times(1)).findCompanyByName(Mockito.argThat(create -> createCompanyRequest.getName().equals("A")));

    }


    @Test
    void get() {

        company = Company.builder()
                .id(1L)
                .name("B")
                .isEnabled(false)
                .build();

        List<Company> companies = new ArrayList<>();
        companies.add(company);
        Page<Company> pagedResponse = new PageImpl<>(companies);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        assertEquals(getCompanyResponseList, companyService.get(1, 0, "desc"));
        verify(companyRepository, times(1)).findAll(any(Pageable.class));

    }
}