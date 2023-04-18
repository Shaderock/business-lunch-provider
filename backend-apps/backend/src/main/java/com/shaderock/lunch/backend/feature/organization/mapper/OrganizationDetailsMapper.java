package com.shaderock.lunch.backend.feature.organization.mapper;

import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.dto.PublicOrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrganizationDetailsMapper {

  @Mapping(target = "usersIds", source = "users")
  OrganizationDetailsDto toDto(OrganizationDetails organizationDetails);

  PublicOrganizationDetailsDto toPublicDto(OrganizationDetails organizationDetails);

  @InheritInverseConfiguration
  OrganizationDetails toEntity(OrganizationDetailsDto organizationDetailsDto);

  default List<UUID> usersToIds(List<AppUser> users) {
    return Stream.ofNullable(users)
        .flatMap(Collection::stream)
        .map(AppUser::getId)
        .toList();
  }

  default List<AppUser> idsToUsers(List<UUID> usersIds) {
    return Stream.ofNullable(usersIds)
        .flatMap(Collection::stream)
        .map(id -> AppUser.baseEntityBuilder().id(id).build())
        .toList();
  }
}
