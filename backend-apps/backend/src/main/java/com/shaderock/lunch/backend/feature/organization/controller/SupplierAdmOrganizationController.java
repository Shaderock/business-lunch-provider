package com.shaderock.lunch.backend.feature.organization.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.organization.dto.PublicOrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
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
@RequestMapping(ApiConstants.SUPPLIER_ADM_ORGANIZATION)
public class SupplierAdmOrganizationController {

  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final SubscriptionService subscriptionService;
  private final SupplierService supplierService;

  @GetMapping("/subscribers")
  public ResponseEntity<List<PublicOrganizationDetailsDto>> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(supplier);
    List<OrganizationDetails> organizationDetailsList = subscriptions.stream()
        .map(Subscription::getCompany)
        .map(Company::getOrganizationDetails)
        .toList();

    return ResponseEntity.ok(
        organizationDetailsList.stream().map(organizationDetailsMapper::toPublicDto).toList());
  }

}

