package com.shaderock.lunch.backend.feature.user.mapper;

import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AppUserMapper {

  @Mapping(target = "detailsId", source = "userDetails.id")
  @Mapping(target = "organizationId", source = "organizationDetails.id")
  @Mapping(target = "organizationRequestId", source = "organizationDetailsRequest.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  AppUserDto toDto(AppUser appUser);

  @InheritInverseConfiguration
  AppUser toEntity(AppUserDto appUserDTO);
}
