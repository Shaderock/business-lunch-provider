package com.shaderock.lunch.backend.organization.supplier.preference.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.supplier.preference.model.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.preference.model.mapper.SupplierPreferencesMapper;
import com.shaderock.lunch.backend.organization.supplier.preference.repository.SupplierPreferencesRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
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

  public SupplierPreferences update(@NonNull SupplierPreferencesDto supplierPreferencesDto,
      @NonNull AppUserDetails userDetails) {
    return update(supplierPreferencesMapper.toEntity(supplierPreferencesDto), userDetails);
  }

  @Transactional
  public SupplierPreferences update(@NonNull SupplierPreferences preferences,
      @NonNull AppUserDetails userDetails) {
    SupplierPreferences persisted = read(userDetails);
    persisted.setRequestOffset(preferences.getRequestOffset());
    persisted.setDeliveryPeriodStartTime(preferences.getDeliveryPeriodStartTime());
    persisted.setDeliveryPeriodEndTime(preferences.getDeliveryPeriodEndTime());
    persisted.setMinimumOrdersPerRequest(preferences.getMinimumOrdersPerRequest());
    persisted.setOrderType(preferences.getOrderType());
    persisted.setOrderCapacity(preferences.getOrderCapacity());

    return persisted;
  }

  @Transactional
  public void delete(@NonNull AppUserDetails userDetails) {
    SupplierPreferences read = read(userDetails);
    supplierPreferencesRepository.delete(read);
  }
}
