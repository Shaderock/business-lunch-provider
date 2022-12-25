package com.shaderock.backend.auth.registration.error.message;

import com.shaderock.backend.messaging.message.ErrorMessage;
import lombok.Getter;

public class TokenNotFoundErrorMessage extends ErrorMessage {
  @Getter
  private static final boolean TOKEN_FOUND = false;
  @Getter
  private final String email;

  public TokenNotFoundErrorMessage(String message, String email) {
    super(message);
    this.email = email;
  }
}
