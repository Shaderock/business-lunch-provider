package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeOrderValidationService {

  private final SubscriptionService subscriptionService;
  private final EmployeeOrderMapper orderMapper;
  private final OptionService optionService;
  private final CompanyService companyService;
  private final List<OrderTypeOrderValidator> orderTypeOrderValidators;

  public void validateCreate(@NonNull EmployeeOrderDto orderDto,
      @NonNull AppUserDetails userDetails) {
    EmployeeOrder order = orderMapper.toEntity(orderDto);
    Supplier supplier = validateSupplier(order);
    validateSubscription(userDetails, supplier);
    validateSupplierPreferences(order, supplier.getPreferences());
  }

  private void validateSupplierPreferences(EmployeeOrder order,
      SupplierPreferences supplierPreferences) {
    OrderTypeOrderValidator orderTypeOrderValidator = orderTypeOrderValidators.stream()
        .filter(validator -> validator.supports(supplierPreferences.getOrderType())).findFirst()
        .orElseThrow(() -> new IllegalStateException(
            String.format("Validator of class [%s] of type [%s] not found",
                OrderTypeOrderValidator.class.getName(), supplierPreferences.getOrderType())));
    orderTypeOrderValidator.validate(order, supplierPreferences);

    Set<Category> uniqueCategories = order.getOptions().stream().map(Option::getCategory)
        .collect(Collectors.toSet());
    if (uniqueCategories.size() < supplierPreferences.getMinimumCategoriesForEmployeeOrder()) {
      throw new CrudValidationException("Minimum required amount of categories ordered is" +
          supplierPreferences.getMinimumCategoriesForEmployeeOrder());
    }

    if (order.getOrderDate().isBefore(LocalDate.now())) {
      throw new CrudValidationException("Can not order for previous days");
    }
  }

  private void validateSubscription(AppUserDetails userDetails, Supplier supplier) {
    Company company = companyService.read(userDetails);
    subscriptionService.read(company, supplier);
  }

  private Supplier validateSupplier(EmployeeOrder order) {
    List<Option> options = order.getOptions().stream()
        .map(option -> optionService.read(option.getId())).toList();

    Set<Supplier> suppliers = options.stream()
        .map(Option::getCategory)
        .map(Category::getMenu)
        .map(Menu::getSupplier)
        .collect(Collectors.toSet());

    if (suppliers.size() > 1) {
      throw new CrudValidationException("Options have different suppliers");
    }

    return suppliers.iterator().next();
  }

  public void validateDelete(@NonNull EmployeeOrder order) {
    if (order.getStatus() == EmployeeOrderStatus.ORDERED) {
      throw new CrudValidationException("Can not delete a confirmed order");
    }
  }


}
