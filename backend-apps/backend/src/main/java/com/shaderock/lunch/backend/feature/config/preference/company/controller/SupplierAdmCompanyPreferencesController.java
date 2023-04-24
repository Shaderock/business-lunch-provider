package com.shaderock.lunch.backend.feature.config.preference.company.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.PublicCompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
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
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SUPPLIER_ADM_COMPANY_PREFERENCES)
public class SupplierAdmCompanyPreferencesController {

  private final SupplierService supplierService;
  private final SubscriptionService subscriptionService;
  private final CompanyPreferencesMapper companyPreferencesMapper;

  @GetMapping("/subscriber/all")
  @Transactional
  public ResponseEntity<List<PublicCompanyPreferencesDto>> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(supplier);
    List<CompanyPreferences> companyPreferencesList = subscriptions.stream()
        .map(Subscription::getCompany)
        .map(Company::getPreferences)
        .toList();
    return ResponseEntity.ok(companyPreferencesList.stream()
        .map(companyPreferencesMapper::toPublicDto)
        .toList());
  }
}
