package com.shaderock.lunch.backend.organization.supplier.model.exception;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;

public class SupplierRegistrationValidationException extends TransferableApplicationException {
  public SupplierRegistrationValidationException(String message) {
    super(message);
  }
}
