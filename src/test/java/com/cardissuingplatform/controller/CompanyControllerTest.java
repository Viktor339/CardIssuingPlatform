package com.cardissuingplatform.controller;

import com.cardissuingplatform.IntegrationTestBase;
import com.cardissuingplatform.controller.dto.ChangeCompanyDto;
import com.cardissuingplatform.controller.request.ChangeCompanyRequest;
import com.cardissuingplatform.controller.request.CreateCompanyRequest;
import com.cardissuingplatform.model.Company;
import com.cardissuingplatform.service.Page;
import com.cardissuingplatform.service.exception.CompanyAlreadyExistsException;
import com.cardissuingplatform.service.exception.ValidatorPageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CompanyControllerTest extends IntegrationTestBase {

    @Autowired
    private CompanyController companyController;

    private CreateCompanyRequest createCompanyRequest;
    private ChangeCompanyRequest changeCompanyRequest;
    private ChangeCompanyDto changeCompanyDto;

    @BeforeEach
    public void setUp() {
        createCompanyRequest = new CreateCompanyRequest();
        createCompanyRequest.setName("A");
        createCompanyRequest.setIsEnabled(true);

        changeCompanyRequest = new ChangeCompanyRequest();
        changeCompanyRequest.setName("D");
        changeCompanyRequest.setIsEnabled(true);

        changeCompanyDto = ChangeCompanyDto.builder()
                .id(1L)
                .name("A")
                .isEnabled(true)
                .build();

    }

    @Test
    public void testCreateShouldThrowCompanyAlreadyExistsException() {

        assertThrows(CompanyAlreadyExistsException.class, () ->
                companyController.create(createCompanyRequest));

    }

    @Test
    public void testCreateShouldReturnId() {

        createCompanyRequest.setName("D");

        Long id = companyController.create(createCompanyRequest);
        assertEquals(4, id);
    }

    @Test
    public void testChangeCompanyAlreadyExistsException() {

        assertThrows(CompanyAlreadyExistsException.class, () ->
                companyController.change(changeCompanyRequest));
    }


    @Test
    public void testChangeCompanyShouldReturnChangeCompanyDto() {

        changeCompanyRequest.setName("A");
        ChangeCompanyDto resultDto = companyController.change(changeCompanyRequest);

        assertEquals(changeCompanyDto, resultDto);
    }

    @Test
    public void testGetShouldThrowValidatorPageException() {

        assertThrows(ValidatorPageException.class, () ->
                companyController.get("asc", 1, 1));
    }

    @Test
    public void testGetShouldReturnPage() {

        Page<Company> companyPage = companyController.get("asc", 10, 0);
        assertEquals(3, companyPage.getItems().size());

    }
}

