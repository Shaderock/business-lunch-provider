package com.shaderock.lunch.backend.auth.registration.error.exception;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import lombok.Getter;

public class ConfirmationEmailNotSentException extends TransferableApplicationException {
  @Getter
  private final String email;

  public ConfirmationEmailNotSentException(String email) {
    super(String.format("Couldn't send confirmation email for User with email=[%s]", email));
    this.email = email;
  }
}
