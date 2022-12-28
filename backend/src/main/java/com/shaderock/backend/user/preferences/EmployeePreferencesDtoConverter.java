package com.shaderock.backend.user.preferences;

import com.shaderock.backend.user.preferences.model.EmployeePreferencesDTO;
import com.shaderock.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeePreferencesDtoConverter {
  @Transactional
  public EmployeePreferencesDTO convertToDto(EmployeePreferenceConfig preferences) {
    if (Objects.isNull(preferences)) {
      return new EmployeePreferencesDTO();
    }

    // todo handle employee notification config
    return new EmployeePreferencesDTO();
  }
}
