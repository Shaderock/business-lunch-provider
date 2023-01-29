package com.shaderock.lunch.backend.messaging.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.messaging.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GeneralControllerAdvice {
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage handleAnyException(Exception e) {
    log.error("An exception happened", e);
    return new ErrorMessage(false, "Something went wrong");
  }

  @ExceptionHandler(TransferableApplicationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleKnownException(TransferableApplicationException e) {
    log.error("An exception happened", e);
    return new ErrorMessage(true, e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleValidationError(MethodArgumentNotValidException ex)
          throws JsonProcessingException {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : fieldErrors) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(errors);
  }
}
