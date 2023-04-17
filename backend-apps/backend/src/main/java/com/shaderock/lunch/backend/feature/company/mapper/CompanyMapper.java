package com.shaderock.lunch.backend.feature.company.mapper;

import com.shaderock.lunch.backend.feature.company.dto.CompanyDto;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
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
  @Mapping(target = "subscriptionsRequestsIds", source = "subscriptionsRequests")
  @Mapping(target = "preferencesId", source = "preferences.id")
  CompanyDto toDto(Company company);

  @InheritInverseConfiguration
  Company toEntity(CompanyDto companyDto);

  default Set<UUID> subscriptionsToIds(Set<Supplier> subscriptions) {
    return Stream.ofNullable(subscriptions)
        .flatMap(Collection::stream)
        .map(Supplier::getId)
        .collect(Collectors.toSet());
  }

  default Set<Supplier> idsToSubscriptions(Set<UUID> subscriptionsIds) {
    return Stream.ofNullable(subscriptionsIds)
        .flatMap(Collection::stream)
        .map(id -> {
          Supplier supplier = new Supplier();
          supplier.setId(id);
          return supplier;
        })
        .collect(Collectors.toSet());
  }
}
