package com.shaderock.lunch.backend.feature.details.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
  USER("USER"),
  EMPLOYEE("EMPLOYEE"),
  COMPANY_ADMIN("COMPANY_ADMIN"),
  SUPPLIER("SUPPLIER"),
  SYSTEM_ADMIN("SYSTEM_ADMIN");

  @Getter
  private final String name;

  public String getNameWithPrefix() {
    return "ROLE_" + name;
  }
}
