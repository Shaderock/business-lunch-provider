package com.shaderock.lunch.backend.feature.order.employee.service.validation;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import java.util.List;
import lombok.NonNull;

interface OrderTypeOrderValidator {

  boolean supports(OrderType orderType);

  List<String> validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences);
}
