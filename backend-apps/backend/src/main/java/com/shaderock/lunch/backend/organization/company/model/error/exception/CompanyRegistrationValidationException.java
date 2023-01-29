package com.shaderock.lunch.backend.organization.company.model.error.exception;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import lombok.Getter;

@Getter
public class CompanyRegistrationValidationException extends TransferableApplicationException {
  public CompanyRegistrationValidationException(String message) {
    super(message);
  }
}
