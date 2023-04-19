package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoriesOrderValidator implements OrderTypeOrderValidator {

  private final OptionService optionService;

  @Override
  public boolean supports(OrderType orderType) {
    return orderType == OrderType.ONLY_ONE_OPTION_PER_CATEGORY;
  }

  @Override
  public void validate(@NonNull EmployeeOrder employeeOrder,
      @NonNull SupplierPreferences supplierPreferences) {
    Set<Category> uniqueCategoriesSet = employeeOrder.getOptions().stream()
        .map(option -> optionService.read(option.getId()))
        .map(Option::getCategory)
        .collect(Collectors.toSet());

    if (uniqueCategoriesSet.size() != employeeOrder.getOptions().size()) {
      throw new CrudValidationException("Only one option per category is supported");
    }
  }
}
