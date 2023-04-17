package com.shaderock.lunch.backend.communication.exception;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CrudValidationException extends ValidationException {

  private final List<String> errors = new ArrayList<>();

  public CrudValidationException(String message) {
    super(message);
  }

  public CrudValidationException(String message, List<String> errors) {
    super(message);
    this.errors.addAll(errors);
  }
}
