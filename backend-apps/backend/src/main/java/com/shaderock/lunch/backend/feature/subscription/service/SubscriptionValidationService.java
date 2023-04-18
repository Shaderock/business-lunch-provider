package com.shaderock.lunch.backend.feature.subscription.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.service.CompanyValidationService;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.repository.SubscriptionRepository;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionValidationService {

  private final SubscriptionRepository subscriptionRepository;
  private final CompanyValidationService companyValidationService;


  public void validateCreate(@NonNull Subscription subscription) {
    if (subscription.getCompany() == null) {
      throw new CrudValidationException("Company to subscribe not provided");
    }
    if (subscription.getSupplier() == null) {
      throw new CrudValidationException("Supplier to subscribe not provided");
    }

    UUID companyId = subscription.getCompany().getId();
    UUID supplierId = subscription.getSupplier().getId();

    if (subscriptionRepository.existsByCompany_IdAndSupplier_Id(companyId, supplierId)) {
      throw new CrudValidationException(
          String.format("Subscription for Company(id=[%s]) and Supplier(id=[%s] already exists",
              companyId, supplierId));
    }

    companyValidationService.validateCompanyProfileIsCompleted(subscription.getCompany());
  }

  public void validateDelete(Subscription subscription) {
    // todo validate unsubscribe (probably not needed)
  }
}
