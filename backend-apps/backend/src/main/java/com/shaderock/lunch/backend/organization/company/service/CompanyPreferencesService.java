package com.shaderock.lunch.backend.organization.company.service;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.repository.CompanyPreferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyPreferencesService {

  private final CompanyPreferenceRepository companyPreferenceRepository;

  @Transactional
  public CompanyPreferences create(CompanyPreferences preferences) {
    // todo validate
    return companyPreferenceRepository.save(preferences);
  }

  @Transactional
  public void delete(CompanyPreferences preferences) {
    // todo validate
    companyPreferenceRepository.delete(preferences);
  }
}
