package com.shaderock.lunch.backend.feature.order.employee.service.validation;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
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
import com.shaderock.lunch.backend.util.FilterManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
  private final FilterManager filterManager;

  public List<String> validateOptionsPublicAndNotDeleted(@NonNull List<UUID> optionsIds) {
    List<String> errors = new ArrayList<>();
    filterManager.ignoreVisibility();
    filterManager.ignoreDeleted();
    for (UUID optionId : optionsIds) {
      Option option = optionService.read(optionId);
      if (option.isDeleted() || !option.isPublic()) {
        errors.add(String.format("%s option not found", option.getName()));
      }
    }
    return errors;
  }

  public List<String> validateCreate(@NonNull EmployeeOrderDto orderDto,
      @NonNull AppUserDetails userDetails) {
    List<String> errors = validateOptionsPublicAndNotDeleted(
        orderDto.optionIds().stream().toList());

    if (!errors.isEmpty()) {
      return errors;
    }

    filterManager.returnNotDeleted();
    filterManager.returnPublic();

    EmployeeOrder order = orderMapper.toEntity(orderDto);
    order.setAppUser(userDetails.getAppUser());
    order.setOptions(
        order.getOptions().stream().map(option -> optionService.read(option.getId())).toList());

    Supplier supplier = validateSupplier(order);
    errors.addAll(validateSubscription(userDetails, supplier));

    Company company = companyService.read(order.getAppUser().getUserDetails());
    LocalTime deliverTime = company.getPreferences().getDeliverAt();
    LocalDateTime probableOrderDateTime = order.getOrderDate().atTime(deliverTime);
    errors.addAll(
        validateSupplierPreferences(order, supplier.getPreferences(), probableOrderDateTime));
    return errors;
  }

  private List<String> validateSupplierPreferences(EmployeeOrder order,
      SupplierPreferences supplierPreferences, LocalDateTime orderDateTime) {
    OrderTypeOrderValidator orderTypeOrderValidator = orderTypeOrderValidators.stream()
        .filter(validator -> validator.supports(supplierPreferences.getOrderType())).findFirst()
        .orElseThrow(() -> new IllegalStateException(
            String.format("Validator of class [%s] of type [%s] not found",
                OrderTypeOrderValidator.class.getName(),
                supplierPreferences.getOrderType())));

    List<String> errors = new ArrayList<>(
        orderTypeOrderValidator.validate(order, supplierPreferences));

    Set<Category> uniqueCategories = order.getOptions().stream()
        .map(Option::getCategory)
        .collect(Collectors.toSet());

    if (supplierPreferences.getOrderType() != OrderType.ONLY_ONE_OPTION &&
        (uniqueCategories.size() < supplierPreferences.getMinimumCategoriesForEmployeeOrder())) {
      errors.add(
          String.format("Minimum required amount of categories ordered is [%s] but only [%s] sent",
              supplierPreferences.getMinimumCategoriesForEmployeeOrder(), uniqueCategories.size()));
    }

    if (order.getOrderDate().isBefore(LocalDate.now())) {
      errors.add("Can not order for previous days");
    }

    Duration inAdvanceDuration = Duration.between(LocalDateTime.now(), orderDateTime);
    Duration requestOffset = supplierPreferences.getRequestOffset();

    LOGGER.info("OrderTime {}, inAdvanceDuration {}, requestOffset {}", orderDateTime,
        inAdvanceDuration, requestOffset);

    if (requestOffset.compareTo(inAdvanceDuration) > 0) {
      errors.add(
          String.format("Request offset of %s is greater than the actual order time in advance",
              supplierPreferences.getSupplier().getOrganizationDetails().getName()));
    }

    // todo validate working hours?

    return errors;
  }

  private List<String> validateSubscription(AppUserDetails userDetails, Supplier supplier) {
    Company company = companyService.read(userDetails);
    subscriptionService.read(company, supplier);
    return new ArrayList<>();
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
    if (order.getStatus() != EmployeeOrderStatus.PENDING_ADMIN_CONFIRMATION) {
      throw new CrudValidationException("Can not delete a confirmed order");
    }
  }
}
