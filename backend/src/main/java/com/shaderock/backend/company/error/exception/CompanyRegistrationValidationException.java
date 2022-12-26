package com.shaderock.backend.company.error.exception;

import com.shaderock.backend.messaging.exception.TransferableApplicationException;

public class CompanyRegistrationValidationException extends TransferableApplicationException {
  public CompanyRegistrationValidationException(String message) {
    super(message);
  }
}
