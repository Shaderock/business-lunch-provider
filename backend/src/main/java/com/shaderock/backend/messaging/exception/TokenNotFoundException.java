package com.shaderock.backend.messaging.exception;

import lombok.Getter;

public class TokenNotFoundException extends RuntimeException {
  @Getter
  private final String token;

  public TokenNotFoundException(String token) {
    super(String.format("User with token=[%s] not found", token));
    this.token = token;
  }
}
