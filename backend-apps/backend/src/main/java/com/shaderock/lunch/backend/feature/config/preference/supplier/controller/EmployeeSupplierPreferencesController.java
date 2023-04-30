package com.shaderock.lunch.backend.feature.config.preference.supplier.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.PublicSupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.mapper.PublicSupplierPreferencesMapper;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.util.ApiConstants;
import com.shaderock.lunch.backend.util.FilterManager;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE_SUPPLIER_PREFERENCES)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmployeeSupplierPreferencesController {

  private final PublicSupplierPreferencesMapper preferencesMapper;
  private final CompanyService companyService;
  private final FilterManager filterManager;

  @GetMapping("/subscription/all")
  @Transactional
  public ResponseEntity<List<PublicSupplierPreferencesDto>> read(Principal principal) {
    filterManager.ignoreVisibility();
    Company company = companyService.read(principal);

    List<PublicSupplierPreferencesDto> supplierPreferencesDtos = company.getSubscriptions().stream()
        .map(Subscription::getSupplier)
        .map(Supplier::getPreferences)
        .map(preferencesMapper::toDto)
        .toList();

    return ResponseEntity.ok(supplierPreferencesDtos);
  }

}
