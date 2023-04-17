package com.shaderock.lunch.backend.feature.config.preference.employee.service;

import com.shaderock.lunch.backend.feature.config.preference.employee.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.config.preference.employee.repository.EmployeePreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeePreferencesService {

  private final EmployeePreferencesRepository employeePreferencesRepository;

  public static EmployeePreferencesDto mapToDto(EmployeePreferences employeePreferences) {
    return EmployeePreferencesDto.builder().build();
  }
}
