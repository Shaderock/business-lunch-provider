package com.shaderock.lunch.backend.organization.supplier;

import com.shaderock.lunch.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.organization.supplier.model.SupplierDTO;
import com.shaderock.lunch.backend.organization.supplier.model.SupplierPreferencesDTO;
import com.shaderock.lunch.backend.organization.supplier.model.SupplierRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.model.exception.SupplierRegistrationValidationException;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferenceConfig;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import com.shaderock.lunch.backend.organization.supplier.preference.repository.SupplierPreferencesRepository;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class SupplierService {
  private final SupplierRepository<Supplier> supplierRepository;
  private final SupplierPreferencesRepository supplierPreferencesRepository;
  private final AppUserDetailsService userDetailsService;

  @Transactional
  public Supplier register(SupplierRegistrationForm form, Principal principal) {
    validateSupplierRegistration(form, principal);

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Supplier supplier = new Supplier();
    supplier.setName(form.name());
    supplier.setEmail(form.email());
    supplier.setDescription(form.description());
    supplier.setWebsiteUrl(URI.create(form.websiteUrl()));
    supplier.setMenuUrl(URI.create(form.menuUrl()));
    supplier.setPhone(form.phone());
    supplier.setDeleted(false);

    supplier = save(supplier);

    SupplierPreferenceConfig preferences = new SupplierPreferenceConfig();
    preferences.setRequestOffset(form.requestOffset());
    preferences.setDeliveryPeriodStartTime(form.deliveryPeriodStartTime());
    preferences.setDeliveryPeriodEndTime(form.deliveryPeriodEndTime());
    preferences.setOrderType(OrderType.UNLIMITED_OPTIONS);
    preferences.setSupplier(supplier);

    preferences = supplierPreferencesRepository.save(preferences);

    supplier.setPreferences(preferences);
    userDetails.getAppUser().setOrganization(supplier);
    return supplier;
  }

  private void validateSupplierRegistration(SupplierRegistrationForm form, Principal principal) {
    if (supplierRepository.findByName(form.name()).isPresent()) {
      throw new SupplierRegistrationValidationException(String.format("Supplier [%s] already exists", form.name()));
    }

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

    if (userDetails.getRoles().size() > 1 && !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(String.format("User [%s] can not create companies",
                                                                     form.email()));
    }
  }

  @Transactional
  public Supplier save(Supplier supplier) {
    return supplierRepository.save(supplier);
  }

  @Transactional
  public SupplierDTO convertToDto(Supplier supplier) {
    SupplierPreferenceConfig preferences = supplier.getPreferences();
    SupplierPreferencesDTO preferencesDto = new SupplierPreferencesDTO(preferences.getRequestOffset(),
                                                                       preferences.getDeliveryPeriodStartTime(),
                                                                       preferences.getDeliveryPeriodEndTime(),
                                                                       preferences.getMinimumOrdersPerRequest(),
                                                                       preferences.getOrderType(),
                                                                       null,
                                                                       null);
    return SupplierDTO.builder()
            .name(supplier.getName())
            .email(supplier.getEmail())
            .description(supplier.getDescription())
            .menuUrl(supplier.getMenuUrl().toString())
            .websiteUrl(supplier.getWebsiteUrl().toString())
            .phone(supplier.getPhone())
            .preferences(preferencesDto)
            .build();
  }
}
