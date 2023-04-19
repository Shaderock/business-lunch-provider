package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import lombok.NonNull;

interface OrderTypeOrderValidator {

  boolean supports(OrderType orderType);

  void validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences);
}
