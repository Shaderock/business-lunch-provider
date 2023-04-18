package com.shaderock.lunch.backend.feature.config.preference.company.service;

import com.shaderock.lunch.backend.feature.config.preference.company.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import java.util.UUID;
import lombok.NonNull;

public interface CompanyPreferencesService {

  CompanyPreferences create(@NonNull CompanyPreferencesDto preferencesDto);

  CompanyPreferences create(@NonNull CompanyPreferences preferences);

  CompanyPreferences read(@NonNull UUID id);

  CompanyPreferences read(AppUserDetails userDetails);

  CompanyPreferences update(@NonNull CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails);

  CompanyPreferences update(@NonNull CompanyPreferences companyPreferences,
      AppUserDetails userDetails);

  void delete(@NonNull CompanyPreferences preferences);
}
