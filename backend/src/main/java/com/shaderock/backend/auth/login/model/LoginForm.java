package com.shaderock.backend.auth.login.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {
  @NotNull(message = "Email is not provided")
  @NotBlank(message = "Email is empty")
  @Email
  private String email;

  @NotNull(message = "Password is not provided")
  @Size(min = 8, max = 25, message = "Password is not between 8 and 25 characters length")
  private String password;
}
