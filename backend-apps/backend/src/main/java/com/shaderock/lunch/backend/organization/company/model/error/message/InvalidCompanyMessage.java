package com.shaderock.lunch.backend.organization.company.model.error.message;

import com.shaderock.lunch.backend.messaging.message.ErrorMessage;

public class InvalidCompanyMessage extends ErrorMessage {

  public InvalidCompanyMessage(String errMessage) {
    super(true, errMessage);
  }
}
