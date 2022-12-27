package com.shaderock.backend.auth.login.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginForm(
        @NotNull(message = "Email should be provided")
        @Email(message = "Wrong email")
        String email,

        @NotNull(message = "Password should be provided")
        @Size(min = 8, max = 25, message = "Password should be between 8 and 25 characters length")
        String password) {
}
