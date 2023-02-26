package com.shaderock.lunch.backend.messaging.exception;

import jakarta.validation.ValidationException;

public class CrudValidationException extends ValidationException {

  public CrudValidationException(String message) {
    super(message);
  }
}
