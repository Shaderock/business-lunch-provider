package com.shaderock.lunch.backend.organization.supplier.controller;

import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
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
@RequestMapping("/api/sysadm/supplier")

public class PrivilegedSupplierController {

  private final SupplierService supplierService;

  @GetMapping
  public ResponseEntity<List<SupplierDto>> readAll() {
    return ResponseEntity.ok(supplierService.readAllAsDto());
  }
}
