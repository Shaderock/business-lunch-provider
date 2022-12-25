package com.shaderock.backend.auth.registration.error.exception;

import lombok.Getter;

public class ConfirmationEmailNotSentException extends RuntimeException {
  @Getter
  private final String email;

  public ConfirmationEmailNotSentException(String email) {
    super(String.format("Couldn't send confirmation email for User with email=[%s]", email));
    this.email = email;
  }
}
