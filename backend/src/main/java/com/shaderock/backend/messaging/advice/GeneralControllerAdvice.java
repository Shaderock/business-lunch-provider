package com.shaderock.backend.messaging.advice;

import com.shaderock.backend.messaging.message.ErrorMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvice {
  @ExceptionHandler(Exception.class)
  public ErrorMessage handleAnyException(Exception e) {
    return ErrorMessage.builder().message(e.getMessage()).build();
  }
}
