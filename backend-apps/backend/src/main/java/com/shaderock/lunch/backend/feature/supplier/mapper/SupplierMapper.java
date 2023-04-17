package com.shaderock.lunch.backend.feature.supplier.mapper;

import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.supplier.dto.PublicSupplierDto;
import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
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
public interface SupplierMapper {

  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "subscribersIds", source = "subscribers")
  @Mapping(target = "preferencesId", source = "preferences.id")
  SupplierDto toDto(Supplier supplier);

  @InheritInverseConfiguration
  Supplier toEntity(SupplierDto supplierDto);

  @Mapping(target = "organizationDetailsId", source = "organizationDetails.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  PublicSupplierDto toPublicDto(Supplier supplier);

  @InheritInverseConfiguration
  Supplier toEntity(PublicSupplierDto supplierDto);

  default Set<UUID> subscribersToIds(Set<Subscription> subscribers) {
    return Stream.ofNullable(subscribers)
        .flatMap(Collection::stream)
        .map(Subscription::getId)
        .collect(Collectors.toSet());
  }

  default Set<Subscription> idsToSubscribers(Set<UUID> subscribersIds) {
    return Stream.ofNullable(subscribersIds)
        .flatMap(Collection::stream)
        .map(id -> Subscription.baseEntityBuilder().id(id).build())
        .collect(Collectors.toSet());
  }

}
