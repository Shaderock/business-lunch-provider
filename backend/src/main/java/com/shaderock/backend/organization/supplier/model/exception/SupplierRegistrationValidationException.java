package com.shaderock.backend.organization.supplier.model.exception;

import com.shaderock.backend.messaging.exception.TransferableApplicationException;

public class SupplierRegistrationValidationException extends TransferableApplicationException {
  public SupplierRegistrationValidationException(String message) {
    super(message);
  }
}
