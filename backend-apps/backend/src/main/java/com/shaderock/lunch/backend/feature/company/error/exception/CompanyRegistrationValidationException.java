package com.shaderock.lunch.backend.feature.company.error.exception;

import com.shaderock.lunch.backend.communication.exception.TransferableApplicationException;
import lombok.Getter;

@Getter
public class CompanyRegistrationValidationException extends TransferableApplicationException {

  public CompanyRegistrationValidationException(String message) {
    super(message);
  }
}
