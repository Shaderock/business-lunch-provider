package com.shaderock.lunch.backend.feature.config.preference.company.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.config.preference.company.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.PublicCompanyPreferencesDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyPreferencesMapper extends BaseMapper {

  @Mapping(source = "company.id", target = "companyId")
  CompanyPreferencesDto toDto(CompanyPreferences companyPreferences);

  @Mapping(target = "companyId", source = "company.id")
  PublicCompanyPreferencesDto toPublicDto(CompanyPreferences companyPreferences);

  @InheritInverseConfiguration
  CompanyPreferences toEntity(CompanyPreferencesDto companyPreferencesDto);
}
