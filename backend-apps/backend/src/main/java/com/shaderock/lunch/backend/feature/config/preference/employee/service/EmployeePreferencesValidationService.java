package com.shaderock.lunch.backend.feature.config.preference.employee.service;

import com.shaderock.lunch.backend.feature.company.service.CompanyValidationService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeePreferencesValidationService {

  private final CompanyValidationService companyValidationService;

  public void validateRead(@NonNull AppUserDetails userDetails) {
    companyValidationService.validateUserIsEmployee(userDetails);
  }
}
