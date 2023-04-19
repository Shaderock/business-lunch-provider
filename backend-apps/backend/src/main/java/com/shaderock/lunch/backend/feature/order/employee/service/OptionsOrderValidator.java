package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OptionsOrderValidator implements OrderTypeOrderValidator {

  @Override
  public boolean supports(OrderType orderType) {
    return orderType == OrderType.UNLIMITED_OPTIONS;
  }

  @Override
  public void validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences) {
    // there are no restrictions on categories for this order type
  }
}
