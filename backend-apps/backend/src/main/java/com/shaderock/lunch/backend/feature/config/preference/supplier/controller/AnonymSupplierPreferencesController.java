package com.shaderock.lunch.backend.feature.config.preference.supplier.controller;

import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.PublicSupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.mapper.PublicSupplierPreferencesMapper;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ANONYM_SUPPLIER_PREFERENCES)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AnonymSupplierPreferencesController {

  private final PublicSupplierPreferencesMapper preferencesMapper;
  private final SupplierService supplierService;

  @GetMapping("/all")
  public ResponseEntity<List<PublicSupplierPreferencesDto>> read() {
    List<SupplierPreferences> preferencesList = supplierService.read().stream()
        .map(Supplier::getPreferences).toList();
    return ResponseEntity.ok(preferencesList.stream().map(preferencesMapper::toDto).toList());
  }

  @GetMapping
  public ResponseEntity<PublicSupplierPreferencesDto> readByName(
      @RequestParam @NotNull String supplierName) {
    Supplier supplier = supplierService.readByName(supplierName);
    return ResponseEntity.ok(preferencesMapper.toDto(supplier.getPreferences()));
  }
}
