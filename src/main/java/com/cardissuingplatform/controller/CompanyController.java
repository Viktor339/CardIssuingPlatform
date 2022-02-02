package com.cardissuingplatform.controller;

import com.cardissuingplatform.controller.dto.ChangeCompanyDto;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.service.CompanyService;
import com.cardissuingplatform.service.Page;
import com.cardissuingplatform.service.PageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
@Validated
@Data
@ConfigurationProperties(prefix = "validator.company.page")
public class CompanyController {

    private final CompanyService companyService;
    private final PageService pageService;

    private Integer min;
    private Integer max;

    @PostMapping
    public Long create(@Valid @RequestBody CreateCompanyRequest createCompanyRequest) {
        return companyService.create(createCompanyRequest);
    }

    @PatchMapping
    public ChangeCompanyDto change(@Valid @RequestBody ChangeCompanyRequest changeCompanyRequest) {
        return companyService.change(changeCompanyRequest);
    }

    @GetMapping
    public Page<Company> get(@RequestParam(name = "sort", defaultValue = "asc") String sort,
                             @RequestParam(name = "size", defaultValue = "10") Integer size,
                             @RequestParam("page") Integer page) {
        pageService.validatePage(size, min, max);
        return companyService.get(size, page, sort);
    }
}
