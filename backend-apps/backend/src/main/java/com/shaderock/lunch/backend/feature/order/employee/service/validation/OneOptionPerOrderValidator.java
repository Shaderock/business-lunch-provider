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
public class OneOptionPerOrderValidator implements OrderTypeOrderValidator {

  @Override
  public boolean supports(OrderType orderType) {
    return orderType == OrderType.ONLY_ONE_OPTION;
  }

  @Override
  public List<String> validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences) {
    if (employeeOrder.getOptions().size() != 1) {
      return List.of(String.format("There should be only one option but %s were sent",
          employeeOrder.getOptions().size()));
    }

    return List.of();
  }
}
