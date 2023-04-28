package com.shaderock.lunch.backend.feature.subscription.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.service.NotificationService;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.repository.SubscriptionRepository;
import com.shaderock.lunch.backend.feature.subscription.type.SubscriptionStatus;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SupplierService supplierService;
  private final CompanyService companyService;
  private final NotificationService notificationService;
  private final SubscriptionValidationService subscriptionValidationService;
  private final SubscriptionRepository subscriptionRepository;

  @Transactional
  public Subscription create(@NonNull Subscription subscription) {
    subscriptionValidationService.validateCreate(subscription);

    if (subscription.getSubscriptionStatus() == SubscriptionStatus.PENDING) {
      Notification notification = Notification.builder().content(
          String.format("There is a new subscription request from Organization (%s).",
              subscription.getCompany().getOrganizationDetails().getName())).build();

      notificationService.create(notification,
          subscription.getSupplier().getOrganizationDetails().getUsers().stream()
              .map(AppUser::getUserDetails).toList(), true);
    }

    return subscriptionRepository.save(subscription);
  }

  public Subscription create(@NonNull Supplier supplier, @NonNull Company company) {
    Subscription subscription = Subscription.builder()
        .company(company)
        .supplier(supplier)
        .subscriptionStatus(SubscriptionStatus.PENDING)
        .build();
    return create(subscription);
  }


  public Subscription read(@NonNull UUID id) {
    return subscriptionRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(String.format("Subscription(id=%s) not found", id)));
  }

  public List<Subscription> read(@NonNull Company company) {
    return subscriptionRepository.findByCompany(company);
  }

  public Subscription read(@NonNull Company company, @NonNull UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    return read(company, supplier);
  }

  public List<Subscription> read(@NonNull Supplier supplier) {
    return subscriptionRepository.findBySupplier(supplier);
  }

  public Subscription read(@NonNull Supplier supplier, @NonNull UUID companyId) {
    Company company = companyService.read(companyId);
    return read(company, supplier);
  }

  public Subscription read(@NonNull Company company, @NonNull Supplier supplier) {
    return subscriptionRepository.findByCompanyAndSupplier(company, supplier).orElseThrow(
        () -> new CrudValidationException(
            String.format("Company(name=[%s]) is not subscribed for Supplier(name=%s)",
                company.getId(), supplier.getId())));
  }

  @Transactional
  public Subscription update(@NonNull Subscription subscription) {
    Subscription persisted = read(subscription.getId());

    if (subscription.getSubscriptionStatus() == SubscriptionStatus.ACCEPTED &&
        persisted.getSubscriptionStatus() == SubscriptionStatus.PENDING) {
      Notification notification = Notification.builder().content(
          String.format("Your organization has subscribed to a supplier (%s). Check new lunches!",
              persisted.getSupplier().getOrganizationDetails().getName())).build();

      notificationService.create(notification,
          subscription.getCompany().getOrganizationDetails().getUsers().stream()
              .map(AppUser::getUserDetails)
              .toList(),
          true);
    }

    persisted.setSubscriptionStatus(subscription.getSubscriptionStatus());

    return persisted;
  }

  @Transactional
  public void delete(@NonNull Subscription subscription) {
    subscriptionValidationService.validateDelete(subscription);

    OrganizationDetails supplierDetails = subscription.getSupplier().getOrganizationDetails();
    OrganizationDetails companyDetails = subscription.getCompany().getOrganizationDetails();
    List<AppUserDetails> companyUserDetailsList = companyDetails.getUsers().stream()
        .map(AppUser::getUserDetails)
        .toList();
    List<AppUserDetails> supplierUserDetailsList = supplierDetails.getUsers().stream()
        .map(AppUser::getUserDetails)
        .toList();
    Notification companyNotification;
    Notification supplierNotification;

    if (subscription.getSubscriptionStatus() == SubscriptionStatus.ACCEPTED) {
      companyNotification = Notification.builder().content(
          String.format("Your organization has unsubscribed from a Supplier (%s)",
              supplierDetails.getName())).build();

      notificationService.create(companyNotification, companyUserDetailsList, true);

      supplierNotification = Notification.builder().content(
          String.format("A Company (%s) was unsubscribed from your organization",
              companyDetails.getName())).build();
    } else {
      companyNotification = Notification.builder().content(
          String.format("Your subscription request for Supplier (%s) was dismissed",
              supplierDetails.getName())).build();

      notificationService.create(companyNotification, companyUserDetailsList, true,
          Set.of(Role.COMPANY_ADMIN));

      supplierNotification = Notification.builder().content(
          String.format("Subscription request from Organization (%s) was dismissed",
              supplierDetails.getName())).build();

    }

    notificationService.create(supplierNotification, supplierUserDetailsList, true);
    subscriptionRepository.delete(subscription);
  }

  @Transactional
  public void delete(@NonNull UUID subscriptionId) {
    Subscription persisted = read(subscriptionId);
    delete(persisted);
  }
}
