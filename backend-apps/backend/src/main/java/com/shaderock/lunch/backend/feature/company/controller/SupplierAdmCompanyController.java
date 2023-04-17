package com.shaderock.lunch.backend.feature.company.controller;

import com.shaderock.lunch.backend.feature.company.dto.PublicCompanyDto;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping(ApiConstants.SUPPLIER_ADMIN_COMPANY)
public class SupplierAdmCompanyController {

  private final CompanyService companyService;
  private final CompanyMapper companyMapper;
  private final SubscriptionService subscriptionService;
  private final SupplierService supplierService;

  @GetMapping("/subscribers")
  public ResponseEntity<List<PublicCompanyDto>> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(supplier);
    List<Company> companies = subscriptions.stream().map(Subscription::getCompany).toList();
    return ResponseEntity.ok(companies.stream().map(companyMapper::toPublicDto).toList());
  }
}
