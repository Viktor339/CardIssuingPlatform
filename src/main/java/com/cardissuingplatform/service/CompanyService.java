package com.cardissuingplatform.service;

import com.cardissuingplatform.controller.dto.ChangeCompanyDto;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.repository.CompanyRepository;
import com.cardissuingplatform.service.exception.CompanyAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;
    @PersistenceContext
    private final EntityManager entityManager;


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

    public ChangeCompanyDto change(ChangeCompanyRequest changeCompanyRequest) {

        Company company = companyRepository.findCompanyByName(changeCompanyRequest.getName())
                .orElseThrow(() -> new CompanyAlreadyExistsException("Company not found"));

        company.setEnabled(changeCompanyRequest.getIsEnabled());

        entityManager.persist(company);

        return ChangeCompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .isEnabled(company.isEnabled())
                .build();
    }


    public Page<Company> get(Integer size, Integer page, String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by("name").descending());
        }
        return new Page<>(companyRepository.findAll(pageable));
    }
}
