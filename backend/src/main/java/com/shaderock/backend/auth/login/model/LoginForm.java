package com.shaderock.backend.auth.login.model;

import jakarta.validation.constraints.NotNull;

public record LoginForm(
        @NotNull(message = "Email should be provided")
        String email,

        @NotNull(message = "Password should be provided")
        String password) {
}
