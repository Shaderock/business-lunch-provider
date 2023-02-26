package com.shaderock.lunch.backend.user.preferences.mapper;

import com.shaderock.lunch.backend.user.preferences.model.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface EmployeePreferencesMapper {

  @Mapping(target = "notificationConfigId", source = "notificationConfig.id")
  @Mapping(target = "employeeId", source = "employee.id")
  EmployeePreferencesDto toDto(EmployeePreferenceConfig employeePreferenceConfig);
}
