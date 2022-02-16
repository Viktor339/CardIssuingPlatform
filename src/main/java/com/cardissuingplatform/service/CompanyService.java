package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.dto.ChangeCompanyResponse;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.controller.response.GetCompanyResponse;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.repository.CompanyRepository;
import com.cardissuingplatform.service.exception.CompanyAlreadyExistsException;
import com.cardissuingplatform.service.exception.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;


    public Long create(CreateCompanyRequest createCompanyRequest) {

        if (companyRepository.findCompanyByName(createCompanyRequest.getName())
                .isPresent()) {
            throw new CompanyAlreadyExistsException("Company already exists");
        }

        Company company = Company.builder()
                .name(createCompanyRequest.getName())
                .isEnabled(createCompanyRequest.getIsEnabled())
                .build();
        companyRepository.save(company);

        return company.getId();
    }

    public ChangeCompanyResponse change(ChangeCompanyRequest changeCompanyRequest) {

        Company company = companyRepository.findCompanyByName(changeCompanyRequest.getName())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setEnabled(changeCompanyRequest.getIsEnabled());

        return ChangeCompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .isEnabled(company.isEnabled())
                .build();
    }


    public List<GetCompanyResponse> get(Integer size, Integer page, String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by("name").descending());
        }

        return companyRepository.findAll(pageable)
                .stream()
                .map(company -> GetCompanyResponse
                        .builder()
                        .id(company.getId())
                        .name(company.getName())
                        .isEnabled(company.isEnabled())
                        .build()).collect(Collectors.toList());
    }
}
