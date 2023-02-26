package com.shaderock.lunch.backend.user.preferences;

import com.shaderock.lunch.backend.user.preferences.model.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeePreferencesService {

  public static EmployeePreferencesDto mapToDto(EmployeePreferenceConfig employeePreferenceConfig) {
    return EmployeePreferencesDto.builder().build();
  }
}
