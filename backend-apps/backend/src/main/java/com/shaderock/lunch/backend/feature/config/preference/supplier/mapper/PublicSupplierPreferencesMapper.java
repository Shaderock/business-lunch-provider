package com.shaderock.lunch.backend.feature.config.preference.supplier.mapper;

import com.shaderock.lunch.backend.feature.config.preference.supplier.dto.PublicSupplierPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PublicSupplierPreferencesMapper {

  @InheritInverseConfiguration
  SupplierPreferences toEntity(PublicSupplierPreferencesDto publicSupplierPreferencesDto);

  PublicSupplierPreferencesDto toDto(SupplierPreferences supplierPreferences);
}
