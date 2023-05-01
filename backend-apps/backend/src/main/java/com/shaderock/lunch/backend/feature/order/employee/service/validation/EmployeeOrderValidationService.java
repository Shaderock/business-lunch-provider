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
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.util.FilterManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  public List<String> validateCreate(@NonNull EmployeeOrderDto orderDto,
      @NonNull AppUserDetails userDetails, Optional<LocalDateTime> orderDateTime) {
    List<String> errors = validateOptionsIdsAndCategoriesPublicAndNotDeleted(
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

    errors.addAll(validateSupplierPreferences(order, supplier.getPreferences(),
        orderDateTime));
    return errors;
  }

  public EmployeeOrderValidationDto validateCreatedOrder(@NonNull EmployeeOrder order,
      @NonNull AppUserDetails userDetails, Optional<LocalDateTime> deliverAt) {
    List<String> errors = new ArrayList<>();

    if (Stream.of(EmployeeOrderStatus.CONFIRMED_BY_SUPPLIER,
            EmployeeOrderStatus.PENDING_SUPPLIER_CONFIRMATION,
            EmployeeOrderStatus.DECLINED_BY_SUPPLIER)
        .noneMatch(status -> order.getStatus() == status)) {

      errors = validateOptionsAndCategoriesPicAndNotDeleted(
          order.getOptions().stream().toList());

      if (errors.isEmpty()) {
        try {
          filterManager.returnNotDeleted();
          filterManager.returnPublic();

          Supplier supplier = validateSupplier(order);
          errors.addAll(validateSubscription(userDetails, supplier));
          errors.addAll(validateSupplierPreferences(order, supplier.getPreferences(), deliverAt));
        } catch (CrudValidationException e) {
          errors = List.of(e.getMessage());
        }
      }
    }

    UUID supplierId = order.getOptions().stream()
        .findFirst()
        .map(option -> option.getCategory().getMenu().getSupplier().getId())
        .orElseThrow(() -> new IllegalStateException("Order not validated"));

    return EmployeeOrderValidationDto.builder()
        .errors(errors)
        .valid(errors.isEmpty())
        .supplierId(supplierId)
        .orderId(order.getId())
        .userDetailsId(userDetails.getId())
        .build();
  }

  public List<String> validateOptionsIdsAndCategoriesPublicAndNotDeleted(
      @NonNull List<UUID> optionsIds) {
    List<String> errors = new ArrayList<>();
    filterManager.ignoreVisibility();
    filterManager.ignoreDeleted();
    for (UUID optionId : optionsIds) {
      Option option = optionService.read(optionId);
      if (option.isDeleted() || !option.isPublic()) {
        errors.add(String.format("%s option not found", option.getName()));
      }
      if (option.getCategory().isDeleted() || !option.getCategory().isPublic()) {
        errors.add(String.format("%s category not found", option.getCategory().getName()));
      }
    }
    return errors;
  }

  public List<String> validateOptionsAndCategoriesPicAndNotDeleted(
      @NonNull List<Option> options) {
    return validateOptionsIdsAndCategoriesPublicAndNotDeleted(
        options.stream().map(Option::getId).toList());
  }

  private List<String> validateSupplierPreferences(EmployeeOrder order,
      SupplierPreferences supplierPreferences, Optional<LocalDateTime> orderDateTime) {
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

    orderDateTime.ifPresent(
        dateTime -> errors.addAll(validateOrderDateTime(supplierPreferences, dateTime)));

    return errors;
  }

  public List<String> validateOrderDateTime(SupplierPreferences supplierPreferences,
      LocalDateTime orderDateTime) {
    List<String> errors = new ArrayList<>();
    if (orderDateTime.toLocalDate().isBefore(LocalDate.now())) {
      errors.add("Can not order for previous days");
    }

    Duration inAdvanceDuration = Duration.between(LocalDateTime.now(), orderDateTime);
    Duration requestOffset = supplierPreferences.getRequestOffset();

    LOGGER.info("OrderTime {}, inAdvanceDuration {}, requestOffset {}", orderDateTime,
        inAdvanceDuration, requestOffset);

    if (requestOffset.compareTo(inAdvanceDuration) > 0) {
      errors.add(
          String.format("Order should be requested in more time in advance for %s",
              supplierPreferences.getSupplier().getOrganizationDetails().getName()));
    }

    if (orderDateTime.toLocalTime().isAfter(supplierPreferences.getWorkDayEnd()) ||
        orderDateTime.toLocalTime().isBefore(supplierPreferences.getWorkDayStart())) {
      errors.add("Supplier is not working at requested time");
    }
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

    Supplier supplier = suppliers.iterator().next();
    if (supplier.isDeleted() || !supplier.isPublic()) {
      throw new CrudValidationException(
          String.format("Supplier %s not found", supplier.getOrganizationDetails().getName()));
    }

    return supplier;
  }

  public void validateDelete(@NonNull EmployeeOrder order) {
    if (order.getStatus() != EmployeeOrderStatus.PENDING_ADMIN_CONFIRMATION) {
      throw new CrudValidationException("Can not delete a confirmed order");
    }
  }
}
