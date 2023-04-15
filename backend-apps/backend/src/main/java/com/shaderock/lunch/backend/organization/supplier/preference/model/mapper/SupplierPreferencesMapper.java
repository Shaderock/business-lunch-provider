package com.shaderock.lunch.backend.organization.supplier.preference.model.mapper;

import com.shaderock.lunch.backend.menu.price.model.entity.PriceForCategories;
import com.shaderock.lunch.backend.organization.supplier.preference.model.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
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
public interface SupplierPreferencesMapper {
  @Mapping(target = "priceForCategoriesIds", source = "priceForCategories")
  @Mapping(target = "orderCapacityId", source = "orderCapacity.id")
  @Mapping(source = "supplier.id", target = "supplierId")
  SupplierPreferencesDto toDto(SupplierPreferences preferences);

  @InheritInverseConfiguration
  SupplierPreferences toEntity(SupplierPreferencesDto preferencesDto);

  default Set<UUID> priceForCategoriesToIds(Set<PriceForCategories> priceForCategories) {
    return Stream.ofNullable(priceForCategories)
        .flatMap(Collection::stream)
        .map(PriceForCategories::getId)
        .collect(Collectors.toSet());
  }

  default Set<PriceForCategories> idsToPriceForCategories(Set<UUID> categoriesIds) {
    return Stream.ofNullable(categoriesIds)
        .flatMap(Collection::stream)
        .map(id -> PriceForCategories.builder().id(id).build())
        .collect(Collectors.toSet());
  }
}
