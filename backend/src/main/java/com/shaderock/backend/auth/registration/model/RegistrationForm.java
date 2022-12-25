package com.shaderock.backend.auth.registration.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {

  @NotNull(message = "Email is not provided")
  @NotBlank(message = "Email is empty")
  @Email
  private String email;

  @NotNull(message = "Password is not provided")
  @Size(min = 8, max = 25, message = "Password is not between 8 and 25 characters length")
  private String password;
  @NotNull(message = "First name is not provided")
  @NotBlank(message = "First name is empty")
  private String firstName;
  @NotNull(message = "Last name is not provided")
  @NotBlank(message = "Last name is empty")
  private String lastName;
}
