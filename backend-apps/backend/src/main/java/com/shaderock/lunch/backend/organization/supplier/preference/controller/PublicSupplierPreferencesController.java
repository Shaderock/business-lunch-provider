package com.shaderock.lunch.backend.organization.supplier.preference.controller;

import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.dto.PublicSupplierPreferencesDto;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.preference.model.mapper.PublicSupplierPreferencesMapper;
import com.shaderock.lunch.backend.organization.supplier.preference.service.SupplierPreferencesService;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.utils.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.PUBLIC_SUPPLIER_PREFERENCES)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PublicSupplierPreferencesController {

  private final PublicSupplierPreferencesMapper preferencesMapper;
  private final SupplierPreferencesService preferencesService;
  private final SupplierService supplierService;

  @GetMapping
  public ResponseEntity<PublicSupplierPreferencesDto> read(
      @RequestParam @NotNull UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    SupplierPreferences preferences = preferencesService.read(supplier);
    PublicSupplierPreferencesDto result = preferencesMapper.toDto(preferences);
    return ResponseEntity.ok(result);
  }
}
