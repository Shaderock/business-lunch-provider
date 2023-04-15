package com.shaderock.lunch.backend.organization.supplier.mapper;

import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
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
public interface SupplierMapper {

  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "subscribersIds", source = "subscribers")
  @Mapping(target = "subscriptionsRequestsIds", source = "subscriptionsRequests")
  @Mapping(target = "menuId", source = "menu.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  SupplierDto toDto(Supplier supplier);

  @InheritInverseConfiguration
  Supplier toEntity(SupplierDto supplierDto);

  default Set<UUID> subscribersToIds(Set<Company> subscribers) {
    return Stream.ofNullable(subscribers)
        .flatMap(Collection::stream)
        .map(Company::getId)
        .collect(Collectors.toSet());
  }

  default Set<Company> idsToSubscribers(Set<UUID> subscribersIds) {
    return Stream.ofNullable(subscribersIds)
        .flatMap(Collection::stream)
        .map(id -> {
          Company company = new Company();
          company.setId(id);
          return company;
        })
        .collect(Collectors.toSet());
  }
}
