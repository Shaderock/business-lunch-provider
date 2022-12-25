package com.shaderock.backend.auth.registration.error.message;

import com.shaderock.backend.messaging.message.ErrorMessage;
import lombok.Getter;

@Getter
public class UserRegisteredErrorMessage extends ErrorMessage {
  private static final boolean USER_REGISTERED = true;
  private final String email;

  public UserRegisteredErrorMessage(String message, String email) {
    super(message);
    this.email = email;
  }
}
