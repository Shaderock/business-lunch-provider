package com.shaderock.backend.auth.registration;

import com.shaderock.backend.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.backend.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.backend.auth.registration.error.message.ConfirmationEmailErrorMessage;
import com.shaderock.backend.auth.registration.error.message.TokenNotFoundErrorMessage;
import com.shaderock.backend.auth.registration.error.message.UserRegisteredErrorMessage;
import com.shaderock.backend.auth.registration.error.exception.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class RegistrationControllerAdvice {
  @ExceptionHandler(UserAlreadyRegisteredException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public UserRegisteredErrorMessage userAlreadyRegistered(UserAlreadyRegisteredException e) {
    return new UserRegisteredErrorMessage(e.getMessage(), e.getEmail());
  }

  @ExceptionHandler(ConfirmationEmailNotSentException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ConfirmationEmailErrorMessage confirmationEmailNotSent(ConfirmationEmailNotSentException e) {
    return new ConfirmationEmailErrorMessage(e.getMessage(), e.getEmail());
  }

  @ExceptionHandler(TokenNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public TokenNotFoundErrorMessage tokenNotFound(TokenNotFoundException e){
    return new TokenNotFoundErrorMessage(e.getMessage(), e.getToken());
  }
}
