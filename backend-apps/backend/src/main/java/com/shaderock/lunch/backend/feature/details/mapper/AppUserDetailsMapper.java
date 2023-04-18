package com.shaderock.lunch.backend.feature.details.mapper;

import com.shaderock.lunch.backend.feature.details.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AppUserDetailsMapper {

  @Mapping(target = "appUserId", source = "appUser.id")
  AppUserDetailsDto toDto(AppUserDetails appUser);

  @InheritInverseConfiguration
  AppUserDetails toEntity(AppUserDetailsDto appUserDTO);
}
