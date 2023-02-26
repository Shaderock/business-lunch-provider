package com.shaderock.lunch.backend.user.mapper;

import com.shaderock.lunch.backend.user.model.dto.AppUserDto;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AppUserMapper {

  @Mapping(target = "detailsId", source = "userDetails.id")
  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "organizationRequestId", source = "organizationRequest.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  AppUserDto toDto(AppUser appUser);

  @InheritInverseConfiguration
  AppUser toEntity(AppUserDto appUserDTO);
}
