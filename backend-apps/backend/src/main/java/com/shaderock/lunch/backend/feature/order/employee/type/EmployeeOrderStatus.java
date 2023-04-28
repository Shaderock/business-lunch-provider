package com.shaderock.lunch.backend.feature.order.employee.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmployeeOrderStatus {
  PENDING_ADMIN_CONFIRMATION("Pending admin confirmation"),
  PENDING_SUPPLIER_CONFIRMATION("Pending supplier confirmation"),
  ORDERED("Ordered");

  @JsonValue
  private final String name;
}
