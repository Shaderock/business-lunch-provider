package com.shaderock.backend.auth.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class RegistrationControllerAdvice {
  @ExceptionHandler(UserAlreadyRegisteredException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public UserRegisteredMessage userAlreadyRegistered(UserAlreadyRegisteredException userAlreadyRegisteredException) {
    return new UserRegisteredMessage(userAlreadyRegisteredException.getMessage());
  }
}
