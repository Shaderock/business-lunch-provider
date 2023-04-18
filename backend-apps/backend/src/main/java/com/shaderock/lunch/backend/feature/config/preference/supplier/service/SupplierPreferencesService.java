package com.shaderock.lunch.backend.feature.config.preference.supplier.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.mapper.SupplierPreferencesMapper;
import com.shaderock.lunch.backend.feature.config.preference.supplier.repository.SupplierPreferencesRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierPreferencesService {

  private final SupplierPreferencesRepository supplierPreferencesRepository;
  private final SupplierPreferencesMapper supplierPreferencesMapper;

  public SupplierPreferences create(@NonNull SupplierPreferencesDto preferencesDto) {
    SupplierPreferences preferences = supplierPreferencesMapper.toEntity(preferencesDto);
    return create(preferences);
  }

  @Transactional
  public SupplierPreferences create(@NonNull SupplierPreferences supplierPreferences) {
    return supplierPreferencesRepository.save(supplierPreferences);
  }

  public SupplierPreferences read(@NonNull AppUserDetails userDetails) {
    return supplierPreferencesRepository.findBySupplier_OrganizationDetails_Users_UserDetails_Email(
        userDetails.getEmail()).orElseThrow(() -> new CrudValidationException(
        String.format("User(email=[%s]) is not a part of a supplier organization",
            userDetails.getEmail())));
  }

  public SupplierPreferences read(@NonNull Supplier supplier) {
    return supplierPreferencesRepository.findBySupplier(supplier)
        .orElseThrow(() -> new CrudValidationException(
            String.format("SupplierPreferences not found for Supplier(id=[%s])",
                supplier.getId())));
  }

  public SupplierPreferences update(@NonNull SupplierPreferencesDto supplierPreferencesDto,
      @NonNull AppUserDetails userDetails) {
    return update(supplierPreferencesMapper.toEntity(supplierPreferencesDto), userDetails);
  }

  // todo validate for nulls. you can't set something back to null
  // todo validate whether there are no invalid options if order type is changed
  @Transactional
  public SupplierPreferences update(@NonNull SupplierPreferences preferences,
      @NonNull AppUserDetails userDetails) {
    SupplierPreferences persisted = read(userDetails);

    persisted.setRequestOffset(preferences.getRequestOffset());
    persisted.setDeliveryPeriodStartTime(preferences.getDeliveryPeriodStartTime());
    persisted.setDeliveryPeriodEndTime(preferences.getDeliveryPeriodEndTime());
    persisted.setMinimumOrdersPerCompanyRequest(preferences.getMinimumOrdersPerCompanyRequest());
    persisted.setOrderType(preferences.getOrderType());

    return persisted;
  }

  public SupplierPreferences update(@NonNull SupplierPreferencesDto supplierPreferencesDto,
      @NonNull Supplier supplier) {
    return update(supplierPreferencesDto,
        supplier.getOrganizationDetails().getUsers().stream().findFirst().orElseThrow(
                () -> new IllegalStateException(
                    String.format("Supplier(id=[%s]) has no users", supplier.getId())))
            .getUserDetails());
  }

  @Transactional
  public void delete(@NonNull AppUserDetails userDetails) {
    SupplierPreferences read = read(userDetails);
    supplierPreferencesRepository.delete(read);
  }
}
