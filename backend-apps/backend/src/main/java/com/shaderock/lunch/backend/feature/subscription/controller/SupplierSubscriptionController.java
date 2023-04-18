package com.shaderock.lunch.backend.feature.subscription.controller;

import com.shaderock.lunch.backend.feature.subscription.dto.SubscriptionDto;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.mapper.SubscriptionMapper;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.feature.subscription.type.SubscriptionStatus;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@RequestMapping(ApiConstants.SUPPLIER_ADM_SUBSCRIPTION)
public class SupplierSubscriptionController {

  private final SubscriptionService subscriptionService;
  private final SubscriptionMapper subscriptionMapper;
  private final SupplierService supplierService;

  @GetMapping("/all")
  public ResponseEntity<List<SubscriptionDto>> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(supplier);
    return ResponseEntity.ok(subscriptions.stream().map(subscriptionMapper::toDto).toList());
  }

  @PostMapping("/accept")
  public ResponseEntity<SubscriptionDto> acceptSubscription(
      @RequestParam @NotNull @Valid UUID companyId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Subscription subscription = subscriptionService.read(supplier, companyId);
    subscription.setSubscriptionStatus(SubscriptionStatus.ACCEPTED);
    Subscription updated = subscriptionService.update(subscription);
    return ResponseEntity.ok(subscriptionMapper.toDto(updated));
  }

  @DeleteMapping
  public ResponseEntity<Void> declineSubscription(@RequestParam @NotNull UUID companyId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Subscription subscription = subscriptionService.read(supplier, companyId);
    subscriptionService.delete(subscription);
    return ResponseEntity.noContent().build();
  }
}
