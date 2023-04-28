package com.shaderock.lunch.backend.feature.order.employee.service.price;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OneOptionPerCategoryPriceCalculator implements OrderTypePriceCalculator {

  @Override
  public boolean supports(OrderType orderType) {
    return orderType == OrderType.ONLY_ONE_OPTION_PER_CATEGORY;
  }

  @Override
  public double calculate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences) {
    int optionsAmount = employeeOrder.getOptions().size();

    return supplierPreferences.getPricesForCategories().stream()
        .filter(priceForCategories -> priceForCategories.getAmount() == optionsAmount)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Order not validated"))
        .getPrice();
  }


}
