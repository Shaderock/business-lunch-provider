package com.shaderock.lunch.backend.doubles;

import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CompanyPreferencesServiceSpy implements CompanyPreferencesService {

  private int createFromDtoCount = 0;
  private int createFromPreferencesCount = 0;
  private int readByIdCount = 0;
  private int readByUserDetailsCount = 0;
  private int updateByDtoCount = 0;
  private int updateByPreferencesCount = 0;
  private int deleteCount = 0;

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferencesDto preferencesDto) {
    createFromDtoCount++;
    return new CompanyPreferences();
  }

  @Override
  public CompanyPreferences create(@NonNull CompanyPreferences preferences) {
    createFromPreferencesCount++;
    return new CompanyPreferences();
  }

  @Override
  public CompanyPreferences read(@NonNull UUID id) {
    readByIdCount++;
    return new CompanyPreferences();
  }

  @Override
  public CompanyPreferences read(AppUserDetails userDetails) {
    readByUserDetailsCount++;
    return new CompanyPreferences();
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails) {
    updateByDtoCount++;
    return new CompanyPreferences();
  }

  @Override
  public CompanyPreferences update(@NonNull CompanyPreferences companyPreferences,
      AppUserDetails userDetails) {
    updateByPreferencesCount++;
    return new CompanyPreferences();
  }

  @Override
  public void delete(@NonNull CompanyPreferences preferences) {
    deleteCount++;
  }
}
