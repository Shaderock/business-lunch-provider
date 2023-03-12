package com.shaderock.lunch.backend.doubles;

import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.model.type.CompanyDiscountType;
import com.shaderock.lunch.backend.organization.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.UUID;
import lombok.NonNull;

public class CompanyPreferencesServiceFake implements CompanyPreferencesService {

  private final CompanyPreferences preferences = CompanyPreferences.builder()
      .id(UUID.randomUUID())
      .companyDiscountType(CompanyDiscountType.PERCENTAGE_FIRST)
      .deliveryAddress("Delivery address")
      .build();

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferencesDto preferencesDto) {
    return preferences;
  }

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferences preferences) {
    return preferences;
  }

  @Override
  public CompanyPreferences read(@NonNull UUID id) {
    return preferences;
  }

  @Override
  public CompanyPreferences read(AppUserDetails userDetails) {
    return preferences;
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails) {
    return preferences;
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferences companyPreferences,
      AppUserDetails userDetails) {
    return preferences;
  }

  @Override
  public void delete(@NonNull CompanyPreferences preferences) {

  }
}
