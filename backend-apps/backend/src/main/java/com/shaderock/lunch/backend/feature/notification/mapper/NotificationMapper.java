package com.shaderock.lunch.backend.feature.notification.mapper;

import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.entity.NotificationDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface NotificationMapper {

  @InheritInverseConfiguration
  Notification toEntity(NotificationDto notificationDto);

  @Mapping(target = "isSentByBrowser", source = "sentByBrowser")
  @Mapping(target = "isViewed", source = "viewed")
  NotificationDto toDto(Notification notification);
}
