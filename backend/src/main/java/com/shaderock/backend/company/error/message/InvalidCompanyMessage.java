package com.shaderock.backend.company.error.message;

import com.shaderock.backend.company.model.CompanyDTO;
import com.shaderock.backend.messaging.message.ErrorMessage;
import lombok.Getter;

public class InvalidCompanyMessage extends ErrorMessage {
  @Getter
  private CompanyDTO company;

  public InvalidCompanyMessage(String errMessage) {
    super(true, errMessage);
  }
}
