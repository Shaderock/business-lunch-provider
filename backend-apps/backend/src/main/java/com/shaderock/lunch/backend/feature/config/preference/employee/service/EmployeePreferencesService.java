package com.shaderock.lunch.backend.feature.config.preference.employee.service;

import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.config.preference.employee.repository.EmployeePreferencesRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeePreferencesService {

  private final EmployeePreferencesRepository employeePreferencesRepository;
  private final AppUserDetailsService userDetailsService;
  private final EmployeePreferencesValidationService employeePreferencesValidationService;

  public EmployeePreferences read(Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    employeePreferencesValidationService.validateRead(userDetails);
    return userDetails.getAppUser().getPreferences();
  }

  public EmployeePreferences create(AppUser persistedUser) {
    if (persistedUser.getPreferences() == null) {
      EmployeePreferences employeePreferences = EmployeePreferences.builder()
          .employee(persistedUser)
          .build();

      EmployeePreferences persistedPreferences = employeePreferencesRepository.save(
          employeePreferences);
      persistedUser.setPreferences(persistedPreferences);

      return persistedPreferences;
    }

    return persistedUser.getPreferences();
  }
}
