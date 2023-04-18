package com.shaderock.lunch.backend.feature.config.preference.company.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.company.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.feature.config.preference.company.repository.CompanyPreferenceRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyPreferencesService {

  private final CompanyPreferenceRepository companyPreferenceRepository;
  private final CompanyPreferencesMapper companyPreferencesMapper;

  public CompanyPreferences create(@NonNull CompanyPreferencesDto preferencesDto) {
    CompanyPreferences preferences = companyPreferencesMapper.toEntity(preferencesDto);
    return create(preferences);
  }

  @Transactional
  public CompanyPreferences create(@NonNull CompanyPreferences preferences) {
    return companyPreferenceRepository.save(preferences);
  }

  public CompanyPreferences read(@NonNull UUID id) {
    return companyPreferenceRepository.findById(id).orElseThrow(() -> new CrudValidationException(
        String.format("Company preferences(id=[%s]) not found", id)));
  }

  public CompanyPreferences read(@NonNull AppUserDetails userDetails) {
    return companyPreferenceRepository.findByCompany_OrganizationDetails_Users_UserDetails_Id(
        userDetails.getId()).orElseThrow(() -> new CrudValidationException(
        String.format("User(email=[%s]) is not a part of a company organization",
            userDetails.getEmail())));
  }

  public CompanyPreferences update(@NonNull CompanyPreferencesDto preferencesDto,
      AppUserDetails userDetails) {
    CompanyPreferences preferences = companyPreferencesMapper.toEntity(preferencesDto);
    return update(preferences, userDetails);
  }

  @Transactional
  public CompanyPreferences update(@NonNull CompanyPreferences companyPreferences,
      AppUserDetails userDetails) {
    CompanyPreferences persistedPreferences = read(userDetails);

    persistedPreferences.setDeliveryAddress(companyPreferences.getDeliveryAddress());
    persistedPreferences.setCompanyDiscountType(companyPreferences.getCompanyDiscountType());
    persistedPreferences.setDiscountPercentageFirstOrder(
        companyPreferences.getDiscountPercentageFirstOrder());
    persistedPreferences.setDiscountFixFirstOrder(companyPreferences.getDiscountFixFirstOrder());
    persistedPreferences.setMaxDiscountFixFirstOrder(
        companyPreferences.getMaxDiscountFixFirstOrder());
    persistedPreferences.setDiscountPerDay(companyPreferences.getDiscountPerDay());

    return persistedPreferences;
  }

  @Transactional
  public void delete(@NonNull CompanyPreferences preferences) {
    CompanyPreferences persistedPreferences = read(preferences.getId());
    companyPreferenceRepository.delete(persistedPreferences);
  }

}
