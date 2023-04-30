package com.shaderock.lunch.backend.feature.supplier.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.mapper.SupplierMapper;
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
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_SUPPLIER)
public class CompanyAdmSupplierController {

  private final CompanyService companyService;
  private final SupplierMapper supplierMapper;
  private final SubscriptionService subscriptionService;
  private final FilterManager filterManager;

  @GetMapping("/subscription/all")
  @Transactional
  public ResponseEntity<List<SupplierDto>> read(Principal principal) {
    filterManager.ignoreVisibility();
    Company company = companyService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(company);

    List<Supplier> suppliers = subscriptions.stream().map(Subscription::getSupplier).toList();
    return ResponseEntity.ok(suppliers.stream().map(supplierMapper::toDto).toList());
  }
}
