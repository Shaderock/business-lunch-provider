package com.shaderock.lunch.backend.feature.order.company.service;

import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.service.validation.EmployeeOrderValidationService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyOrderValidationService {

  private final EmployeeOrderValidationService employeeOrderValidationService;

  public CompanyOrderValidationDto validateCreate(CompanyOrder order) {
    List<String> errors = new ArrayList<>();
    List<EmployeeOrderValidationDto> employeeOrderValidations = new ArrayList<>();

    if (order.getEmployeesOrders().isEmpty()) {
      errors.add("Company order has no employees orders");
    } else {
      Supplier supplier = order.getEmployeesOrders().stream()
          .map(EmployeeOrder::getOptions)
          .flatMap(Collection::stream)
          .map(Option::getCategory)
          .map(Category::getMenu)
          .map(Menu::getSupplier)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Order not validated"));

      for (EmployeeOrder employeeOrder : order.getEmployeesOrders()) {
        employeeOrderValidations.add(
            employeeOrderValidationService.validateCreatedOrder(employeeOrder,
                employeeOrder.getAppUser().getUserDetails(), Optional.empty()));

        if (!employeeOrderValidations.stream().allMatch(EmployeeOrderValidationDto::isValid)) {
          errors.add("There are invalid employees orders");
        }

        errors.addAll(
            employeeOrderValidationService.validateOrderDateTime(supplier.getPreferences(),
                order.getDeliverAt()));
      }

      if (supplier.getPreferences().getMinimumOrdersPerCompanyRequest() < order.getEmployeesOrders()
          .size()) {
        errors.add(String.format("There should be at least %s orders but only %s sent",
            supplier.getPreferences().getMinimumOrdersPerCompanyRequest(),
            order.getEmployeesOrders().size()));
      }
    }

    return CompanyOrderValidationDto
        .builder()
        .valid(errors.isEmpty())
        .errors(errors)
        .build();
  }
}
