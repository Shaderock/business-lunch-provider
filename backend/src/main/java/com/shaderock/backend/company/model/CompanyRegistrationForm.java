package com.shaderock.backend.company.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompanyRegistrationForm {
  @NotNull
  @NotBlank
  private String name;
  @NotNull
  @NotBlank
  @Email
  private String email;
  @NotNull
  @NotBlank
  @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
  private String phone;
}
