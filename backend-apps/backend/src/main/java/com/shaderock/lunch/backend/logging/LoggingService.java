package com.shaderock.lunch.backend.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingService {

  public void logRequest(HttpServletRequest httpServletRequest, Object body) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> parameters = buildParametersMap(httpServletRequest);

    stringBuilder.append("REQUEST ");
    stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
    stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
    stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

    if (!parameters.isEmpty()) {
      stringBuilder.append("parameters=[").append(parameters).append("] ");
    }

    if (body != null) {
      stringBuilder.append("body=[").append(body).append("]");
    }

    log.info(stringBuilder.toString());
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {

    String stringBuilder = "RESPONSE "
        + "method=[" + httpServletRequest.getMethod() + "] "
        + "path=[" + httpServletRequest.getRequestURI() + "] "
        + "responseHeaders=[" + buildHeadersMap(httpServletResponse)
        + "] "
        + "responseBody=[" + body + "] ";

    log.info(stringBuilder);
  }

  private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
    Map<String, String> resultMap = new HashMap<>();
    Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

    while (parameterNames.hasMoreElements()) {
      String key = parameterNames.nextElement();
      String value = httpServletRequest.getParameter(key);
      resultMap.put(key, value);
    }

    return resultMap;
  }

  private Map<String, String> buildHeadersMap(HttpServletRequest request) {
    Map<String, String> map = new HashMap<>();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String key = headerNames.nextElement();
      String value = request.getHeader(key);
      map.put(key, value);
    }

    return map;
  }

  private Map<String, String> buildHeadersMap(HttpServletResponse response) {
    Map<String, String> map = new HashMap<>();

    Collection<String> headerNames = response.getHeaderNames();
    for (String header : headerNames) {
      map.put(header, response.getHeader(header));
    }

    return map;
  }
}
