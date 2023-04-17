package com.shaderock.lunch.backend.feature.organization.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record OrganizationRegistrationForm(
    @NotNull(message = "Name should be provided")
    @NotBlank(message = "Name can't be blank")
    String name) implements Serializable {

}
