package com.shaderock.lunch.backend.feature.auth.registration.error.message;

import com.shaderock.lunch.backend.communication.message.ErrorMessage;
import lombok.Getter;

public class TokenNotFoundErrorMessage extends ErrorMessage {

  @Getter
  private static final boolean TOKEN_FOUND = false;
  @Getter
  private final String email;

  public TokenNotFoundErrorMessage(String message, String email) {
    super(true, message);
    this.email = email;
  }
}
