package com.shaderock.lunch.backend.feature.config.preference.supplier.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderType {
  UNLIMITED_OPTIONS("No restrictions"),
  ONLY_ONE_OPTION_PER_CATEGORY("One option per one category"),
  ONLY_ONE_OPTION("Only one option per order");

  @JsonValue
  private final String name;
}
