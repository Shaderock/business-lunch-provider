package com.shaderock.lunch.backend.feature.auth.registration.error.message;

import com.shaderock.lunch.backend.communication.message.ErrorMessage;
import lombok.Getter;

public class ConfirmationEmailErrorMessage extends ErrorMessage {

  @Getter
  private static final boolean CONFIRMATION_EMAIL_SENT = false;
  @Getter
  private final String email;

  public ConfirmationEmailErrorMessage(String message, String email) {
    super(true, message);
    this.email = email;
  }
}
