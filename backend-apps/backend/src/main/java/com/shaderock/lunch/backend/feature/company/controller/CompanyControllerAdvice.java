package com.shaderock.lunch.backend.feature.company.controller;

import com.shaderock.lunch.backend.feature.company.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.feature.company.error.message.InvalidCompanyMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyControllerAdvice {

  @ExceptionHandler(CompanyRegistrationValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public InvalidCompanyMessage invalidCompanyRegistrationAttempt(
      CompanyRegistrationValidationException e) {
    return new InvalidCompanyMessage(e.getMessage());
  }
}
