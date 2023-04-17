package com.shaderock.lunch.backend.feature.company.error.message;

import com.shaderock.lunch.backend.communication.message.ErrorMessage;

public class InvalidCompanyMessage extends ErrorMessage {

  public InvalidCompanyMessage(String errMessage) {
    super(true, errMessage);
  }
}
