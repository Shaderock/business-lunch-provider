package com.shaderock.lunch.backend.feature.config.preference.supplier.controller;

import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.mapper.SupplierPreferencesMapper;
import com.shaderock.lunch.backend.feature.config.preference.supplier.service.SupplierPreferencesService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
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
public class SupplierAdmPreferencesController {

  private final SupplierPreferencesMapper preferencesMapper;
  private final SupplierService supplierService;
  private final SupplierPreferencesService supplierPreferencesService;

  @GetMapping("/my")
  public ResponseEntity<SupplierPreferencesDto> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    SupplierPreferences preferences = supplier.getPreferences();
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }

  @PutMapping
  @Transactional
  public ResponseEntity<SupplierPreferencesDto> update(Principal principal,
      @NotNull @Valid @RequestBody SupplierPreferencesDto supplierPreferencesDto) {
    Supplier supplier = supplierService.read(principal);
    SupplierPreferences preferences = supplierPreferencesService.update(supplierPreferencesDto,
        supplier);
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }
}
