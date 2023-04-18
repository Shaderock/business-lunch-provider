package com.shaderock.lunch.backend.feature.user.mapper;

import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
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
public interface AppUserMapper {

  @Mapping(target = "notificationConfigId", source = "notificationConfig.id")
  @Mapping(target = "invitationsIds", source = "notifications")
  @Mapping(target = "detailsId", source = "userDetails.id")
  @Mapping(target = "organizationId", source = "organizationDetails.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  AppUserDto toDto(AppUser appUser);

  @InheritInverseConfiguration
  AppUser toEntity(AppUserDto appUserDTO);

  default List<UUID> usersToIds(List<Notification> notifications) {
    return Stream.ofNullable(notifications)
        .flatMap(Collection::stream)
        .map(Notification::getId)
        .toList();
  }

  default List<Notification> idsToUsers(List<UUID> invitationsIds) {
    return Stream.ofNullable(invitationsIds)
        .flatMap(Collection::stream)
        .map(id -> Notification.builder().id(id).build())
        .toList();
  }
}
