package com.shaderock.lunch.backend.organization.mapper;

import com.shaderock.lunch.backend.organization.model.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OrganizationDetailsMapper {

  @Mapping(target = "usersIds", source = "users")
  @Mapping(target = "usersRequestsIds", source = "usersRequests")
  OrganizationDetailsDto toDto(OrganizationDetails organizationDetails);

  @InheritInverseConfiguration
  OrganizationDetails toEntity(OrganizationDetailsDto organizationDetailsDto);

  default Set<UUID> usersToIds(Set<AppUser> users) {
    return Stream.ofNullable(users)
        .flatMap(Collection::stream)
        .map(AppUser::getId)
        .collect(Collectors.toSet());
  }

  default Set<AppUser> idsToUsers(Set<UUID> usersIds) {
    return Stream.ofNullable(usersIds)
        .flatMap(Collection::stream)
        .map(id -> AppUser.builder().id(id).build())
        .collect(Collectors.toSet());
  }
}
