package com.shaderock.lunch.backend.doubles;

import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.UUID;
import lombok.NonNull;

public class CompanyPreferencesServiceDummy implements CompanyPreferencesService {

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferencesDto preferencesDto) {
    return null;
  }

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferences preferences) {
    return null;
  }

  @Override
  public CompanyPreferences read(@NonNull UUID id) {
    return null;
  }

  @Override
  public CompanyPreferences read(@NonNull AppUserDetails userDetails) {
    return null;
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails) {
    return null;
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferences companyPreferences,
      AppUserDetails userDetails) {
    return null;
  }

  @Override
  public void delete(@NonNull CompanyPreferences preferences) {

  }
}
