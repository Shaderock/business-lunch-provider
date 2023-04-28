package com.shaderock.lunch.backend.feature.order.employee.service.price;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import lombok.NonNull;

public interface OrderTypePriceCalculator {

  boolean supports(OrderType orderType);

  double calculate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences);

}
