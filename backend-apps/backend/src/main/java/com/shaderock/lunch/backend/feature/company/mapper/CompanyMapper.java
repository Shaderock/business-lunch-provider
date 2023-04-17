package com.shaderock.lunch.backend.feature.company.mapper;

import com.shaderock.lunch.backend.feature.company.dto.CompanyDto;
import com.shaderock.lunch.backend.feature.company.dto.PublicCompanyDto;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
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
public interface CompanyMapper {

  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "subscriptionsIds", source = "subscriptions")
  @Mapping(target = "preferencesId", source = "preferences.id")
  CompanyDto toDto(Company company);

  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  PublicCompanyDto toPublicDto(Company company);

  @InheritInverseConfiguration
  Company toEntity(CompanyDto companyDto);

  default Set<UUID> subscriptionsToIds(Set<Subscription> subscriptions) {
    return Stream.ofNullable(subscriptions)
        .flatMap(Collection::stream)
        .map(Subscription::getId)
        .collect(Collectors.toSet());
  }

  default Set<Subscription> idsToSubscriptions(Set<UUID> subscriptionsIds) {
    return Stream.ofNullable(subscriptionsIds)
        .flatMap(Collection::stream)
        .map(id -> Subscription.baseEntityBuilder().id(id).build())
        .collect(Collectors.toSet());
  }
}
