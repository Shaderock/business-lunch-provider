package com.shaderock.lunch.backend.auth.registration.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationForm(
        @Email(message = "Wrong email")
        String email,
        @NotNull(message = "Password was not provided")
        @Size(min = 8, max = 25, message = "Password is not between 8 and 25 characters length")
        String password,
        @NotBlank(message = "First name is empty")
        String firstName,
        @NotBlank(message = "Last name is empty")
        String lastName) {
}
