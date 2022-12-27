package com.shaderock.backend.company.error.message;

import com.shaderock.backend.messaging.message.ErrorMessage;

public class InvalidCompanyMessage extends ErrorMessage {

  public InvalidCompanyMessage(String errMessage) {
    super(true, errMessage);
  }
}
