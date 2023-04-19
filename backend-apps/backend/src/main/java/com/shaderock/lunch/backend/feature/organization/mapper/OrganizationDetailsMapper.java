package com.shaderock.lunch.backend.feature.organization.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.dto.PublicOrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrganizationDetailsMapper extends BaseMapper {

  @Mapping(target = "usersIds", source = "users")
  OrganizationDetailsDto toDto(OrganizationDetails organizationDetails);

  PublicOrganizationDetailsDto toPublicDto(OrganizationDetails organizationDetails);

  @InheritInverseConfiguration
  OrganizationDetails toEntity(OrganizationDetailsDto organizationDetailsDto);

}
