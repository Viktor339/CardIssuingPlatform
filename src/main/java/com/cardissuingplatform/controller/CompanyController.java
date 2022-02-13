package com.cardissuingplatform.controller;

import com.cardissuingplatform.config.PageProperties;
import com.cardissuingplatform.controller.dto.ChangeCompanyResponse;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.controller.response.GetCompanyResponse;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.service.CompanyService;
import com.cardissuingplatform.service.Page;
import com.cardissuingplatform.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/accountant/v1/companies")
@PreAuthorize("hasRole('ADMIN')")
public class CompanyController {

    private final CompanyService companyService;
    private final PageService pageService;
    private final PageProperties pageProperties;

    @PostMapping
    public Long create(@Valid @RequestBody CreateCompanyRequest createCompanyRequest) {
        return companyService.create(createCompanyRequest);
    }

    @PatchMapping
    public ChangeCompanyResponse change(@Valid @RequestBody ChangeCompanyRequest changeCompanyRequest) {
        return companyService.change(changeCompanyRequest);
    }

    @GetMapping
    public Page<GetCompanyResponse> get(@RequestParam(name = "sort", defaultValue = "asc") String sort,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                                        @RequestParam("page") Integer page) {
        Integer validatedSize = pageService.validatePageSize(size, pageProperties.getMin(), pageProperties.getMax());
        return companyService.get(validatedSize, page, sort);
    }
}
