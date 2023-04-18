package com.shaderock.lunch.backend.feature.config.preference.supplier.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.SupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SupplierPreferencesMapper extends BaseMapper {

  @Mapping(target = "minimumOrdersPerCompanyRequest", source = "minimumOrdersPerCompanyRequest")
  @Mapping(target = "priceForCategoriesIds", source = "priceForCategories")
  @Mapping(target = "orderCapacityId", source = "orderCapacity.id")
  @Mapping(source = "supplier.id", target = "supplierId")
  SupplierPreferencesDto toDto(SupplierPreferences preferences);

  @InheritInverseConfiguration
  SupplierPreferences toEntity(SupplierPreferencesDto preferencesDto);

}
