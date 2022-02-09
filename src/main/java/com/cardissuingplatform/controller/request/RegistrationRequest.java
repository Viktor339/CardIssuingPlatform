package com.cardissuingplatform.controller.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class RegistrationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Min(1)
    private Long roleId;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Min(1)
    private Long companyId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

}
