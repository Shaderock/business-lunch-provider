package com.shaderock.lunch.backend.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

  private final LoggingService loggingService;

  @Override
  public boolean supports(MethodParameter methodParameter,
      Class<? extends HttpMessageConverter<?>> aClass) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object o,
      MethodParameter methodParameter,
      MediaType mediaType,
      Class<? extends HttpMessageConverter<?>> aClass,
      ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {

    if (serverHttpRequest instanceof ServletServerHttpRequest request &&
        serverHttpResponse instanceof ServletServerHttpResponse response) {
      loggingService.logResponse(request.getServletRequest(), response.getServletResponse(), o);
    }

    return o;
  }
}
