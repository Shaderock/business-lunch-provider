package com.shaderock.backend.auth.registration;

import lombok.Getter;

public class UserAlreadyRegisteredException extends RuntimeException {
  @Getter
  private final String email;

  public UserAlreadyRegisteredException(String email) {
    super(String.format("User with email=[%s] is already registered", email));
    this.email = email;
  }
}
