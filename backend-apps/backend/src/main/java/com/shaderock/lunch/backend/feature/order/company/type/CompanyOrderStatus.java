package com.shaderock.lunch.backend.feature.order.company.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CompanyOrderStatus {
  PENDING_SUPPLIER_CONFIRMATION("Pending supplier confirmation"),
  CONFIRMED_BY_SUPPLIER("Confirmed"),
  DECLINED_BY_SUPPLIER("Declined");

  @JsonValue
  private final String name;
}
