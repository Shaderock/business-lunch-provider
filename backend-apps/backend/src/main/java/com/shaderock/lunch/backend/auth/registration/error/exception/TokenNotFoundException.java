package com.shaderock.lunch.backend.auth.registration.error.exception;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import lombok.Getter;

public class TokenNotFoundException extends TransferableApplicationException {

  @Getter
  private final String token;

  public TokenNotFoundException(String token) {
    super(String.format("User with token=[%s] not found", token));
    this.token = token;
  }
}
