package com.shaderock.backend.company;

import com.shaderock.backend.company.error.exception.CompanyRegistrationValidationException;
import com.shaderock.backend.company.error.message.InvalidCompanyMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyControllerAdvice {
  @ExceptionHandler(CompanyRegistrationValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public InvalidCompanyMessage invalidCompanyRegistrationAttempt(CompanyRegistrationValidationException e) {
    return new InvalidCompanyMessage(e.getMessage());
  }
}
