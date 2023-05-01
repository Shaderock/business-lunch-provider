package com.shaderock.lunch.backend.feature.order.employee.controller;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import com.shaderock.lunch.backend.feature.order.employee.service.validation.EmployeeOrderValidationService;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE_ORDER)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmployeeOrderController {

  private final EmployeeOrderMapper employeeOrderMapper;
  private final EmployeeOrderService employeeOrderService;
  private final EmployeeOrderValidationService employeeOrderValidationService;
  private final AppUserDetailsService userDetailsService;
  private final CompanyService companyService;

  @PostMapping
  @Transactional
  public ResponseEntity<EmployeeOrderDto> create(@RequestBody @NotNull EmployeeOrderDto orderDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    preValidateOrder(orderDto, userDetails);

    EmployeeOrder order = employeeOrderService.create(orderDto, userDetails, Optional.empty());
    return ResponseEntity.ok(employeeOrderMapper.toDto(order));
  }

  @PostMapping("/calculate")
  @Transactional
  public ResponseEntity<EmployeeOrderDto> preCalculate(
      @RequestBody @NotNull EmployeeOrderDto orderDto, Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    preValidateOrder(orderDto, userDetails);
    EmployeeOrder order = employeeOrderService.calculateValidOrder(orderDto, userDetails);

    return ResponseEntity.ok(employeeOrderMapper.toDto(order));
  }

  private void preValidateOrder(EmployeeOrderDto orderDto, AppUserDetails userDetails) {
    List<String> errors;

    try {
      Company company = companyService.read(userDetails);
      LocalTime deliverTime = company.getPreferences().getDeliverAt();
      LocalDateTime probableOrderDateTime = orderDto.orderDate().atTime(deliverTime);
      errors = employeeOrderValidationService.validateCreate(orderDto, userDetails,
          Optional.of(probableOrderDateTime));
    } catch (CrudValidationException e) {
      errors = List.of(e.getMessage());
    }

    if (!errors.isEmpty()) {
      throw new CrudValidationException("Order is invalid", errors);
    }
  }

  @PostMapping("/validate")
  @Transactional
  public ResponseEntity<EmployeeOrderValidationDto> validate(
      @RequestBody @NotNull EmployeeOrderDto orderDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);

    List<String> errors;

    try {
      Company company = companyService.read(userDetails);
      LocalTime deliverTime = company.getPreferences().getDeliverAt();
      LocalDateTime probableOrderDateTime = orderDto.orderDate().atTime(deliverTime);
      errors = employeeOrderValidationService.validateCreate(orderDto, userDetails,
          Optional.of(probableOrderDateTime));
    } catch (CrudValidationException e) {
      errors = List.of(e.getMessage());
    }

    return ResponseEntity.ok(EmployeeOrderValidationDto.builder()
        .errors(errors)
        .valid(errors.isEmpty())
        .build());
  }

  @GetMapping
  @Transactional
  // todo check if filter disabling is required here
  public ResponseEntity<List<EmployeeOrderDto>> read(
      @RequestParam(required = false) Optional<EmployeeOrderStatus> status,
      @RequestParam(required = false) Optional<LocalDate> date,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    List<EmployeeOrder> orders = employeeOrderService.read(userDetails, status, date);
    return ResponseEntity.ok(orders.stream().map(employeeOrderMapper::toDto).toList());
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID orderId,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    EmployeeOrder order = employeeOrderService.read(orderId, userDetails);
    employeeOrderService.delete(order);
    return ResponseEntity.noContent().build();
  }
}
