package com.shaderock.lunch.backend.feature.supplier.controller;

import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SYS_ADM_SUPPLIER)

public class SysAdmSupplierController {

  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;

  @GetMapping
  public ResponseEntity<List<SupplierDto>> readAll() {
    List<Supplier> suppliers = supplierService.read();
    return ResponseEntity.ok(suppliers.stream().map(supplierMapper::toDto).toList());
  }
}
