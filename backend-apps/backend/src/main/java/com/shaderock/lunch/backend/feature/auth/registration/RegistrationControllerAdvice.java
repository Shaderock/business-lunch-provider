package com.shaderock.lunch.backend.feature.auth.registration;

import com.shaderock.lunch.backend.communication.message.ErrorMessage;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.TokenNotFoundException;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.lunch.backend.feature.auth.registration.error.message.ConfirmationEmailErrorMessage;
import com.shaderock.lunch.backend.feature.auth.registration.error.message.TokenNotFoundErrorMessage;
import com.shaderock.lunch.backend.feature.auth.registration.error.message.UserRegisteredErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RegistrationControllerAdvice {

  @ExceptionHandler(UserAlreadyRegisteredException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public UserRegisteredErrorMessage userAlreadyRegistered(UserAlreadyRegisteredException e) {
    return new UserRegisteredErrorMessage(e.getMessage(), e.getEmail());
  }

  @ExceptionHandler(ConfirmationEmailNotSentException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ConfirmationEmailErrorMessage confirmationEmailNotSent(
      ConfirmationEmailNotSentException e) {
    return new ConfirmationEmailErrorMessage(e.getMessage(), e.getEmail());
  }

  @ExceptionHandler(TokenNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public TokenNotFoundErrorMessage tokenNotFound(TokenNotFoundException e) {
    return new TokenNotFoundErrorMessage(e.getMessage(), e.getToken());
  }

  @ExceptionHandler(DisabledException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorMessage emailNotConfirmedForLoginAttempt(DisabledException e) {
    LOGGER.error(e.getMessage());
    return new ErrorMessage(true, "Email is not confirmed yet");
  }
}
