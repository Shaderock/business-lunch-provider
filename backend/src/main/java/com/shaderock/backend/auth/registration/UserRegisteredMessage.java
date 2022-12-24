package com.shaderock.backend.auth.registration;

import com.shaderock.backend.messaging.message.ErrorMessage;
import lombok.Getter;

@Getter
public class UserRegisteredMessage extends ErrorMessage {
  private static final boolean USER_REGISTERED = true;

  public UserRegisteredMessage(String message) {
    super(message);
  }
}
