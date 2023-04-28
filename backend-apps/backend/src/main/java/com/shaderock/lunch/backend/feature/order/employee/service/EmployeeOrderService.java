package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.repository.EmployeeOrderRepository;
import com.shaderock.lunch.backend.feature.order.employee.service.discount.CompanyDiscountTypeCalculator;
import com.shaderock.lunch.backend.feature.order.employee.service.price.OrderTypePriceCalculator;
import com.shaderock.lunch.backend.feature.order.employee.service.validation.EmployeeOrderValidationService;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeOrderService {

  private final EmployeeOrderValidationService orderValidationService;
  private final EmployeeOrderRepository orderRepository;
  private final EmployeeOrderMapper orderMapper;
  private final OptionService optionService;
  private final List<OrderTypePriceCalculator> orderTypePriceCalculators;
  private final List<CompanyDiscountTypeCalculator> companyDiscountCalculators;
  private final CompanyService companyService;

  public EmployeeOrder create(@NonNull EmployeeOrderDto orderDto,
      @NonNull AppUserDetails userDetails) {
    orderValidationService.validateCreate(orderDto, userDetails);
    EmployeeOrder order = orderMapper.toEntity(orderDto);
    order.setAppUser(userDetails.getAppUser());
    order.setOptions(order.getOptions().stream()
        .map(option -> optionService.read(option.getId()))
        .toList());
    order.setStatus(EmployeeOrderStatus.PENDING_ADMIN_CONFIRMATION);
    order.setSupplierDefaultPrice(calculateDefaultPrice(order.getOptions()));
    order.setSupplierDiscount(5); // todo

    // todo calculate final price correctly, consider to correctly calculate discounts
    double finalPrice =
        order.getSupplierDefaultPrice() - order.getSupplierDiscount() - order.getCompanyDiscount();
    if (finalPrice < 0) {
      finalPrice = 0;
    }
    order.setFinalPrice(finalPrice);

    return orderRepository.save(order);
  }

  // todo
  private double calculateDefaultPrice(Collection<Option> options) {
    return options.stream()
        .mapToDouble(Option::getPrice)
        .sum();
  }

  public List<EmployeeOrder> read(@NonNull AppUserDetails userDetails,
      @NonNull Optional<EmployeeOrderStatus> status, @NonNull Optional<LocalDate> date) {
    if (status.isEmpty() && date.isEmpty()) {
      return orderRepository.findByAppUser_UserDetails(userDetails);
    }

    if (status.isEmpty()) {
      return orderRepository.findByAppUser_UserDetailsAndOrderDate(userDetails, date.get());
    }

    if (date.isEmpty()) {
      return orderRepository.findByAppUser_UserDetailsAndStatus(userDetails, status.get());
    }

    return orderRepository.findByAppUser_UserDetailsAndStatusAndOrderDate(userDetails, status.get(),
        date.get());
  }

  public EmployeeOrder read(@NonNull UUID orderId, @NonNull AppUserDetails userDetails) {
    return orderRepository.findByIdAndAppUser_UserDetails(orderId, userDetails).orElseThrow(() ->
        new CrudValidationException(
            String.format("Could not find order(id=[%s) for user(email=[%s])", orderId,
                userDetails.getEmail())));
  }

  public void delete(@NonNull EmployeeOrder order) {
    orderValidationService.validateDelete(order);
    orderRepository.delete(order);
  }

  public EmployeeOrder calculateValidOrder(@NonNull EmployeeOrderDto orderDto,
      AppUserDetails userDetails) {
    EmployeeOrder order = orderMapper.toEntity(orderDto);
    order.setAppUser(userDetails.getAppUser());
    order.setOptions(order.getOptions().stream()
        .map(option -> optionService.read(option.getId()))
        .toList());

    Option option = order.getOptions().stream().findFirst()
        .orElseThrow(() -> new IllegalStateException("Order not validated"));
    Supplier supplier = option.getCategory().getMenu().getSupplier();
    SupplierPreferences supplierPreferences = supplier.getPreferences();
    Company company = companyService.read(userDetails);
    CompanyPreferences companyPreferences = company.getPreferences();

    OrderTypePriceCalculator orderTypePriceCalculator = orderTypePriceCalculators.stream()
        .filter(c -> c.supports(supplierPreferences.getOrderType()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            String.format("Price calculator of class [%s] of type [%s] not found",
                OrderTypePriceCalculator.class.getName(), supplierPreferences.getOrderType())));

    CompanyDiscountTypeCalculator companyDiscountCalculator = companyDiscountCalculators.stream()
        .filter(c -> c.supports(companyPreferences.getCompanyDiscountType()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            String.format("Discount calculator of class [%s] of type [%s] not found",
                CompanyDiscountTypeCalculator.class.getName(),
                companyPreferences.getCompanyDiscountType())));

    double supplierDefaultPrice = orderTypePriceCalculator.calculate(order, supplierPreferences);
    order.setSupplierDefaultPrice(supplierDefaultPrice);

    double supplierDiscount = 0; // todo implement
    order.setSupplierDiscount(supplierDiscount);

    double companyDiscount = companyDiscountCalculator.calculate(order, companyPreferences);
    order.setCompanyDiscount(companyDiscount);

    double finalPrice = supplierDefaultPrice - supplierDiscount - companyDiscount;
    order.setFinalPrice(finalPrice);

    return order;
  }
}
