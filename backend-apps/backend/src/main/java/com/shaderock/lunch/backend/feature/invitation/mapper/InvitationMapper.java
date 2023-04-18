package com.shaderock.lunch.backend.feature.invitation.mapper;

import com.shaderock.lunch.backend.feature.invitation.dto.InvitationDto;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface InvitationMapper {

  @Mapping(target = "companyId", source = "company.id")
  @Mapping(target = "appUserId", source = "appUser.id")
  InvitationDto toDto(Invitation invitation);

  @InheritInverseConfiguration
  Invitation toEntity(InvitationDto invitationDto);
}