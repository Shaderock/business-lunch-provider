package com.shaderock.lunch.backend.feature.config.preference.employee.mapper;

import com.shaderock.lunch.backend.feature.config.preference.employee.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface EmployeePreferencesMapper {

  @Mapping(target = "employeeId", source = "employee.id")
  EmployeePreferencesDto toDto(EmployeePreferences employeePreferences);
}
