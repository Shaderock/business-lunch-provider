package com.shaderock.backend.organization.company.model.error.message;

import com.shaderock.backend.messaging.message.ErrorMessage;

public class InvalidCompanyMessage extends ErrorMessage {

  public InvalidCompanyMessage(String errMessage) {
    super(true, errMessage);
  }
}
