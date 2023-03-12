package com.shaderock.lunch.backend.organization.company.preference.model.mapper;

import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyPreferencesMapper {

  @Mapping(source = "company.id", target = "companyId")
  CompanyPreferencesDto toDto(CompanyPreferences companyPreferences);

  @InheritInverseConfiguration
  CompanyPreferences toEntity(CompanyPreferencesDto companyPreferencesDto);
}
