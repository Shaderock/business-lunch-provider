package com.shaderock.lunch.backend.organization.supplier.preference.controller;

import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.preference.model.mapper.SupplierPreferencesMapper;
import com.shaderock.lunch.backend.organization.supplier.preference.service.SupplierPreferencesService;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.utils.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.SUPPLIER_ADM_PREFERENCES)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierPreferencesController {

  private final SupplierPreferencesMapper preferencesMapper;
  private final SupplierService supplierService;
  private final SupplierPreferencesService supplierPreferencesService;

  @GetMapping
  public ResponseEntity<SupplierPreferencesDto> read(Principal principal) {
    Supplier supplier = supplierService.read(principal.getName());
    SupplierPreferences preferences = supplierPreferencesService.read(supplier);
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }

  @PutMapping
  public ResponseEntity<SupplierPreferencesDto> update(Principal principal,
      @NotNull @Valid @RequestBody SupplierPreferencesDto supplierPreferencesDto) {
    Supplier supplier = supplierService.read(principal.getName());
    SupplierPreferences preferences = supplierPreferencesService.update(supplierPreferencesDto,
        supplier);
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }
}
