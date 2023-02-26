package com.shaderock.lunch.backend.organization.supplier.controller;

import com.shaderock.lunch.backend.organization.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.model.form.SupplierRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/supplier")
public class SupplierController {

  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;

  @PostMapping("/register")
  public ResponseEntity<SupplierDto> register(
      @RequestBody @Valid final SupplierRegistrationForm form,
      Principal principal) {
    Supplier registeredSupplier = supplierService.register(form, principal);
    SupplierDto responseSupplier = supplierMapper.toDto(registeredSupplier);
    return ResponseEntity.ok(responseSupplier);
  }
}
