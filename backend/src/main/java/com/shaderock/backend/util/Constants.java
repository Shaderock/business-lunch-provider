package com.shaderock.backend.util;

import java.util.stream.Stream;

public record Constants() {
  public static final String[] SWAGGER_PATHS = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**",
          "/webjars/swagger-ui/**", "/v3/api-docs.yaml"};
  public static final String[] AUTH_PATHS = {"/api/login/**", "/api/register/**"};
  public static final String[] FOOD_PATHS = {"/api/food/**"};
  public static final String[] PERMIT_ALL_PATHS = Stream.of(SWAGGER_PATHS, AUTH_PATHS, FOOD_PATHS)
          .flatMap(Stream::of)
          .toArray(String[]::new);
}
