package com.shaderock.lunch.backend.auth.registration.error.exception;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import lombok.Getter;

public class UserAlreadyRegisteredException extends TransferableApplicationException {

  @Getter
  private final String email;

  public UserAlreadyRegisteredException(String email) {
    super(String.format("User with email=[%s] is already registered", email));
    this.email = email;
  }
}
