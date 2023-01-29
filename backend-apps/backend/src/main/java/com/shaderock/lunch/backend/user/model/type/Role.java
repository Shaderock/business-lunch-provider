package com.shaderock.lunch.backend.user.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
  USER("USER"), EMPLOYEE("EMPLOYEE"), COMPANY_ADMIN("COMPANY_ADMIN"), SUPPLIER("SUPPLIER");

  @Getter
  private final String name;

  public String getNameWithPrefix() {
    return "ROLE_" + name;
  }
}
