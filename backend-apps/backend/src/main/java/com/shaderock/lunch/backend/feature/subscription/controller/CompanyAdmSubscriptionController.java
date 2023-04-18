package com.shaderock.lunch.backend.feature.subscription.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.subscription.dto.SubscriptionDto;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.mapper.SubscriptionMapper;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_SUBSCRIPTION)
public class CompanyAdmSubscriptionController {

  private final SubscriptionService subscriptionService;
  private final CompanyService companyService;
  private final SupplierService supplierService;
  private final SubscriptionMapper subscriptionMapper;

  @GetMapping("/all")
  public ResponseEntity<List<SubscriptionDto>> read(Principal principal) {
    Company company = companyService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(company);
    return ResponseEntity.ok(subscriptions.stream().map(subscriptionMapper::toDto).toList());
  }

  @PostMapping("/subscribe")
  public ResponseEntity<SubscriptionDto> requestSubscription(@RequestParam @NotNull UUID supplierId,
      Principal principal) {
    Company company = companyService.read(principal);
    Supplier supplier = supplierService.read(supplierId);
    Subscription subscription = subscriptionService.create(supplier, company);
    return ResponseEntity.ok(subscriptionMapper.toDto(subscription));
  }

  @DeleteMapping("/unsubscribe")
  public ResponseEntity<Void> unsubscribe(@RequestParam @NotNull UUID supplierId,
      Principal principal) {
    Company company = companyService.read(principal);
    Subscription subscription = subscriptionService.read(company, supplierId);
    subscriptionService.delete(subscription);
    return ResponseEntity.noContent().build();
  }
}
