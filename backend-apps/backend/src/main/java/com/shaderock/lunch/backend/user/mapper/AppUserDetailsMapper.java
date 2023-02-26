package com.shaderock.lunch.backend.user.mapper;

import com.shaderock.lunch.backend.user.model.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
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
