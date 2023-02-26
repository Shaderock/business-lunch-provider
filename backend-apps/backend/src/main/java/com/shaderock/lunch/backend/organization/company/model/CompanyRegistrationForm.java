package com.shaderock.lunch.backend.organization.company.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CompanyRegistrationForm(
    @NotBlank(message = "Name should be provided")
    String name,
    @NotBlank(message = "Email should be provided")
    @Email(message = "Not an email")
    String email,
    @NotNull(message = "Phone number should be provided")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Not a phone number (e.g. +37312345678)")
    String phone) {

}
