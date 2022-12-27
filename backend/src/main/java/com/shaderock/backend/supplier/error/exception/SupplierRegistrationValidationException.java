package com.shaderock.backend.supplier.error.exception;

import com.shaderock.backend.messaging.exception.TransferableApplicationException;

public class SupplierRegistrationValidationException extends TransferableApplicationException {
  public SupplierRegistrationValidationException(String message) {
    super(message);
  }
}
