package com.shaderock.lunch.backend.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

  private final LoggingService loggingService;

  private final HttpServletRequest httpServletRequest;

  @Override
  public boolean supports(MethodParameter methodParameter, Type type,
      Class<? extends HttpMessageConverter<?>> aClass) {
    return true;
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
      MethodParameter parameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    loggingService.logRequest(httpServletRequest, body);

    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}
