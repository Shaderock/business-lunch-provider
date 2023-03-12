package com.shaderock.lunch.backend.doubles;

import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.model.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.organization.company.preference.model.type.CompanyDiscountType;
import com.shaderock.lunch.backend.organization.company.preference.repository.CompanyPreferenceRepository;
import com.shaderock.lunch.backend.organization.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.UUID;

public class CompanyPreferencesServiceStub implements CompanyPreferencesService {

  private final CompanyPreferenceRepository companyPreferenceRepository;
  private final CompanyPreferencesMapper companyPreferencesMapper;

  public CompanyPreferencesServiceStub(CompanyPreferenceRepository companyPreferenceRepository,
      CompanyPreferencesMapper companyPreferencesMapper) {
    this.companyPreferenceRepository = companyPreferenceRepository;
    this.companyPreferencesMapper = companyPreferencesMapper;
  }

  @Override
  public CompanyPreferences create(CompanyPreferencesDto preferencesDto) {
    CompanyPreferences preferences = companyPreferencesMapper.toEntity(preferencesDto);
    return create(preferences);
  }

  @Override
  public CompanyPreferences create(CompanyPreferences preferences) {
    // In this stub, we're returning a new instance of CompanyPreferences with the same data as the input preferences
    return CompanyPreferences.builder()
        .id(preferences.getId())
        .deliveryAddress(preferences.getDeliveryAddress())
        .companyDiscountType(preferences.getCompanyDiscountType())
        .build();
  }

  @Override
  public CompanyPreferences read(UUID id) {
    // In this stub, we're returning a new instance of CompanyPreferences with a predefined ID
    return CompanyPreferences.builder().id(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"))
        .deliveryAddress("123 Main Street")
        .companyDiscountType(CompanyDiscountType.PERCENTAGE_FIRST)
        .build();
  }

  @Override
  public CompanyPreferences read(AppUserDetails userDetails) {
    // In this stub, we're returning a new instance of CompanyPreferences with a predefined ID and user details
    return CompanyPreferences.builder().id(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"))
        .deliveryAddress("123 Main Street")
        .companyDiscountType(CompanyDiscountType.PERCENTAGE_FIRST)
        .build();
  }

  @Override
  public CompanyPreferences update(CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails) {
    CompanyPreferences preferences = companyPreferencesMapper.toEntity(preferencesDto);
    return update(preferences, userDetails);
  }

  @Override
  public CompanyPreferences update(CompanyPreferences companyPreferences,
      AppUserDetails userDetails) {
    // In this stub, we're returning a new instance of CompanyPreferences with the updated data
    return CompanyPreferences.builder()
        .id(companyPreferences.getId())
        .deliveryAddress(companyPreferences.getDeliveryAddress())
        .companyDiscountType(companyPreferences.getCompanyDiscountType())
        .build();
  }

  @Override
  public void delete(CompanyPreferences preferences) {
    // In this stub, we're doing nothing since we're not actually deleting anything
  }
}

