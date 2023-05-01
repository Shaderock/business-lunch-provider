package com.shaderock.lunch.backend.feature.order.company.controller;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderDto;
import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderValidationDto;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrderMapper;
import com.shaderock.lunch.backend.feature.order.company.service.CompanyOrderService;
import com.shaderock.lunch.backend.feature.order.company.service.CompanyOrderValidationService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.COMPANY_ADM_COMPANY_ORDER)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
public class CompanyAdmCompanyOrderController {

  private final CompanyOrderMapper companyOrderMapper;
  private final CompanyOrderValidationService companyOrderValidationService;
  private final CompanyOrderService companyOrderService;
  private final CompanyService companyService;

  @PostMapping
  @Transactional
  public ResponseEntity<CompanyOrderDto> create(@RequestBody @NotNull CompanyOrderDto orderDto,
      Principal principal) {
    Company company = companyService.read(principal);
    preValidateOrder(orderDto, company);

    CompanyOrder order = companyOrderService.create(orderDto, company);
    return ResponseEntity.ok(companyOrderMapper.toDto(order));
  }

  private void preValidateOrder(CompanyOrderDto orderDto, Company company) {
    CompanyOrder order = companyOrderMapper.toEntity(orderDto);
    order = companyOrderService.convertMappedEmployeesOrdersToValidEmployeesOrders(order, company);
    CompanyOrderValidationDto companyOrderValidationDto = companyOrderValidationService
        .validateCreate(order);

    if (!companyOrderValidationDto.isValid()) {
      throw new CrudValidationException("Order is invalid", companyOrderValidationDto.getErrors());
    }
  }


  @PostMapping("/validate")
  @Transactional
  public ResponseEntity<CompanyOrderValidationDto> validate(
      @RequestBody @NotNull CompanyOrderDto orderDto, Principal principal) {
    LOGGER.info("DeliverAt=[{}]", orderDto.deliverAt());
    Company company = companyService.read(principal);
    CompanyOrder order = companyOrderMapper.toEntity(orderDto);
    order = companyOrderService.convertMappedEmployeesOrdersToValidEmployeesOrders(order, company);
    CompanyOrderValidationDto companyOrderValidationDto = companyOrderValidationService
        .validateCreate(order);
    return ResponseEntity.ok(companyOrderValidationDto);
  }

  @GetMapping
  @Transactional
  // todo check if filter disabling is required here
  public ResponseEntity<List<CompanyOrderDto>> read(
      @RequestParam LocalDate date,
      Principal principal) {
    Company company = companyService.read(principal);
    List<CompanyOrder> orders = companyOrderService.read(date, company);
    return ResponseEntity.ok(orders.stream().map(companyOrderMapper::toDto).toList());
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID orderId,
      Principal principal) {
    Company company = companyService.read(principal);
    CompanyOrder order = companyOrderService.read(orderId, company);
    companyOrderService.delete(order);
    return ResponseEntity.noContent().build();
  }
}
