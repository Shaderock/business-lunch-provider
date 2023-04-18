package com.shaderock.lunch.backend.feature.company.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.company.dto.CompanyDto;
import com.shaderock.lunch.backend.feature.company.dto.PublicCompanyDto;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyMapper extends BaseMapper {

  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "subscriptionsIds", source = "subscriptions")
  @Mapping(target = "preferencesId", source = "preferences.id")
  CompanyDto toDto(Company company);

  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  PublicCompanyDto toPublicDto(Company company);

  @InheritInverseConfiguration
  Company toEntity(CompanyDto companyDto);


}
