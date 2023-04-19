package com.shaderock.lunch.backend.feature.order.employee.controller;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderValidationService;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
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

  @PostMapping
  public ResponseEntity<EmployeeOrderDto> create(@RequestBody @NotNull EmployeeOrderDto orderDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    EmployeeOrder order = employeeOrderService.create(orderDto, userDetails);
    return ResponseEntity.ok(employeeOrderMapper.toDto(order));
  }

  @GetMapping("/validate")
  public ResponseEntity<Void> validate(@RequestBody @NotNull EmployeeOrderDto orderDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    employeeOrderValidationService.validateCreate(orderDto, userDetails);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
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
  public ResponseEntity<Void> delete(@RequestBody @NotNull UUID orderId,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    EmployeeOrder order = employeeOrderService.read(orderId, userDetails);
    employeeOrderService.delete(order);
    return ResponseEntity.noContent().build();
  }
}
