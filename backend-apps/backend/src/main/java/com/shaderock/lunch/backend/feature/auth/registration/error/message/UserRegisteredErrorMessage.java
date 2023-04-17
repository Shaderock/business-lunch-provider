package com.shaderock.lunch.backend.feature.auth.registration.error.message;

import com.shaderock.lunch.backend.communication.message.ErrorMessage;
import lombok.Getter;

@Getter
public class UserRegisteredErrorMessage extends ErrorMessage {

  private static final boolean USER_REGISTERED = true;
  private final String email;

  public UserRegisteredErrorMessage(String message, String email) {
    super(true, message);
    this.email = email;
  }
}
