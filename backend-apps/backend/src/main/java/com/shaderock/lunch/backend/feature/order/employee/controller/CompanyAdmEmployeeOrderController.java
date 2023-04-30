package com.shaderock.lunch.backend.feature.order.employee.controller;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.food.option.repository.OptionRepository;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.repository.EmployeeOrderRepository;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import com.shaderock.lunch.backend.feature.order.employee.service.validation.EmployeeOrderValidationService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.COMPANY_ADM_EMPLOYEE_ORDER)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CompanyAdmEmployeeOrderController {

  private final EmployeeOrderMapper employeeOrderMapper;
  private final EmployeeOrderService employeeOrderService;
  private final EmployeeOrderValidationService employeeOrderValidationService;
  private final AppUserDetailsService userDetailsService;
  private final OptionRepository optionRepository;
  private final EmployeeOrderRepository orderRepository;
  private final CompanyService companyService;

  @PostMapping("/validate-multiple")
  @Transactional
  public ResponseEntity<List<EmployeeOrderValidationDto>> validate(
      @RequestBody List<UUID> ordersIds, Principal principal,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
    Company company = companyService.read(principal);
    List<EmployeeOrder> employeeOrders = employeeOrderService.read(ordersIds, company);
    List<EmployeeOrderValidationDto> responseValidations = new ArrayList<>();

    for (EmployeeOrder employeeOrder : employeeOrders) {
      AppUserDetails userDetails = employeeOrder.getAppUser().getUserDetails();

      EmployeeOrderValidationDto employeeOrderValidationDto =
          employeeOrderValidationService.validateCreatedOrder(employeeOrder, userDetails,
              Optional.of(dateTime));

      responseValidations.add(employeeOrderValidationDto);
    }

    return ResponseEntity.ok(responseValidations);
  }

  @PostMapping("/validate-single")
  @Transactional
  public ResponseEntity<EmployeeOrderValidationDto> validate(
      @RequestBody UUID orderId, Principal principal,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
    Company company = companyService.read(principal);
    EmployeeOrder employeeOrder = employeeOrderService.read(orderId, company);

    AppUserDetails userDetails = employeeOrder.getAppUser().getUserDetails();

    EmployeeOrderValidationDto employeeOrderValidationDto =
        employeeOrderValidationService.validateCreatedOrder(employeeOrder, userDetails,
            Optional.of(dateTime));

    return ResponseEntity.ok(employeeOrderValidationDto);
  }

  @GetMapping
  @Transactional
  // todo check if filter disabling is required here
  public ResponseEntity<List<EmployeeOrderDto>> read(
      @RequestParam @NotNull LocalDate date, Principal principal) {
    Company company = companyService.read(principal);
    List<EmployeeOrder> orders = employeeOrderService.read(company, date);
    return ResponseEntity.ok(orders.stream().map(employeeOrderMapper::toDto).toList());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<EmployeeOrderDto> create(@RequestBody @NotNull EmployeeOrderDto orderDto,
      Principal principal) {
    Company companyAdminCompany = companyService.read(principal);
    AppUserDetails userDetails = userDetailsService.read(orderDto.userDetailsId());
    Company userCompany = companyService.read(userDetails);

    if (userCompany.getId() != companyAdminCompany.getId()) {
      throw new CrudValidationException("User is not part of a company");
    }

    preValidateOrder(orderDto, userDetails);

    EmployeeOrder order = employeeOrderService.create(orderDto, userDetails);
    return ResponseEntity.ok(employeeOrderMapper.toDto(order));
  }

  private void preValidateOrder(EmployeeOrderDto orderDto, AppUserDetails userDetails) {
    List<String> errors;

    try {
      errors = employeeOrderValidationService.validateCreate(orderDto, userDetails);
    } catch (CrudValidationException e) {
      errors = List.of(e.getMessage());
    }

    if (!errors.isEmpty()) {
      throw new CrudValidationException("Order is invalid", errors);
    }
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<EmployeeOrderDto> delete(@RequestParam @NotNull UUID orderId,
      Principal principal) {
    Company company = companyService.read(principal);
    EmployeeOrder order = employeeOrderService.read(orderId, company);

    employeeOrderService.deleteForce(order);
    return ResponseEntity.noContent().build();
  }
}
