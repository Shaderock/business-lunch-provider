package com.shaderock.lunch.backend.communication.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.communication.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.communication.message.ErrorMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GeneralControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage handleAnyException(Exception e) {
    LOGGER.error("An exception happened", e);
    return new ErrorMessage(false, "Something went wrong");
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorMessage badCredentials(BadCredentialsException e) {
    LOGGER.error(e.getMessage());
    return new ErrorMessage(true, e.getMessage());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMissedRequestParameter(MissingServletRequestParameterException e) {
    LOGGER.warn("A parameter was not present in the request. Message: {}", e.getMessage());
    return new ErrorMessage(true, e.getMessage());
  }

  @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMissedRequestParameter(InvalidFormatException e) {
    LOGGER.warn("A wrong format request. Message: {}", e.getMessage());
    return new ErrorMessage(true, "Invalid format of a value received");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMissedRequestParameter(HttpMessageNotReadableException e) {
    LOGGER.warn("Could not read http message. Message: {}", e.getMessage());
    return new ErrorMessage(true, "Could not read http message");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
    LOGGER.warn("Request method is not supported. Message: {}", e.getMessage());
    return new ErrorMessage(false, e.getMessage());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleUsernameNotFound(UsernameNotFoundException e) {
    LOGGER.warn("User not found. Message: {}", e.getMessage());
    return new ErrorMessage(true, e.getMessage());
  }

  @ExceptionHandler(TransferableApplicationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleKnownException(TransferableApplicationException e) {
    LOGGER.warn("An exception happened, message=[{}]", e.getMessage());
    return new ErrorMessage(true, e.getMessage());
  }

  @ExceptionHandler(CrudValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleKnownException(CrudValidationException e) {
    LOGGER.warn("A validation exception happened, message=[{}]", e.getMessage());
    return new ErrorMessage(true, e.getMessage(), e.getErrors());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleValidationError(MethodArgumentNotValidException ex)
      throws JsonProcessingException {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    LOGGER.info("Adding field errors to response [{}]", fieldErrors);
    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : fieldErrors) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(errors);
  }
}
