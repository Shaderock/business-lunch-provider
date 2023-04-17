package com.shaderock.lunch.backend.feature.subscription.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.notification.service.NotificationService;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.repository.SubscriptionRepository;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.feature.user.service.AppUserDetailsService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionValidationService {

  private final SupplierService supplierService;
  private final CompanyService companyService;
  private final AppUserDetailsService userDetailsService;
  private final NotificationService notificationService;
  private final SubscriptionRepository subscriptionRepository;

  public void validateCreate(@NonNull Subscription subscription) {
    if (subscription.getCompany() == null) {
      throw new CrudValidationException("Company to subscribe not provided");
    }
    if (subscription.getSubscriptionStatus() == null) {
      throw new CrudValidationException("Supplier to subscribe not provided");
    }

    UUID companyId = subscription.getCompany().getId();
    UUID supplierId = subscription.getSupplier().getId();

    if (subscriptionRepository.existsByCompany_IdAndSupplier_Id(companyId, supplierId)) {
      throw new CrudValidationException(
          String.format("Subscription for Company(id=[%s]) and Supplier(id=[%s] already exists",
              companyId, supplierId));
    }
  }

  public void validateDelete(Subscription subscription) {
    // todo validate unsubscribe (probably not needed)
  }
}
