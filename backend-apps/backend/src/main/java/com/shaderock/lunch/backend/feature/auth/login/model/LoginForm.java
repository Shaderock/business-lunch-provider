package com.shaderock.lunch.backend.feature.auth.login.model;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record LoginForm(
    @NotNull(message = "Email should be provided")
    String email,

    @NotNull(message = "Password should be provided")
    String password) implements Serializable {

}
