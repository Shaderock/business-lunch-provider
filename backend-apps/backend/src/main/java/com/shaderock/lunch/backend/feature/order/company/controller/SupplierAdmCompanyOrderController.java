package com.shaderock.lunch.backend.feature.order.company.controller;

import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderDto;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrderMapper;
import com.shaderock.lunch.backend.feature.order.company.service.CompanyOrderService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.SUPPLIER_ADM_COMPANY_ORDER)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierAdmCompanyOrderController {

  private final CompanyOrderMapper companyOrderMapper;
  private final CompanyOrderService companyOrderService;
  private final SupplierService supplierService;

  @GetMapping
  @Transactional
  public ResponseEntity<List<CompanyOrderDto>> read(@RequestParam @NotNull LocalDate date,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<CompanyOrder> orders = companyOrderService.read(date, supplier);
    return ResponseEntity.ok(orders.stream().map(companyOrderMapper::toDto).toList());
  }

  @PostMapping("/confirm")
  @Transactional
  public ResponseEntity<Void> confirmOrder(@RequestParam @NotNull UUID orderId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    companyOrderService.confirmOrder(orderId, supplier);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/decline")
  @Transactional
  public ResponseEntity<Void> declineOrder(@RequestParam @NotNull UUID orderId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    companyOrderService.declineOrder(orderId, supplier);
    return ResponseEntity.noContent().build();
  }
}
