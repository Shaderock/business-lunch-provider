package com.shaderock.lunch.backend.feature.order.employee.service.validation;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UnlimitedOptionsOrderValidator implements OrderTypeOrderValidator {

  @Override
  public boolean supports(OrderType orderType) {
    return orderType == OrderType.UNLIMITED_OPTIONS;
  }

  @Override
  public List<String> validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences) {
    // there are no restrictions on categories for this order type
    return List.of();
  }
}
