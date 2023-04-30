package com.shaderock.lunch.backend.feature.order.employee.controller;


import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.SUPPLIER_ADM_EMPLOYEE_ORDER)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierAdmEmployeeOrderController {

  private final EmployeeOrderMapper employeeOrderMapper;
  private final EmployeeOrderService employeeOrderService;
  private final SupplierService supplierService;

  @GetMapping
  @Transactional
  public ResponseEntity<List<EmployeeOrderDto>> read(@RequestParam @NotNull LocalDate date,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<EmployeeOrder> orders = employeeOrderService.read(supplier, date);
    return ResponseEntity.ok(orders.stream().map(employeeOrderMapper::toDto).toList());
  }
}
