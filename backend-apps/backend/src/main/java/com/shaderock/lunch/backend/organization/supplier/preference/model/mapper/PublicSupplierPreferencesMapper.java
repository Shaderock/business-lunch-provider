package com.shaderock.lunch.backend.organization.supplier.preference.model.mapper;

import com.shaderock.lunch.backend.organization.supplier.preference.model.dto.PublicSupplierPreferencesDto;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PublicSupplierPreferencesMapper {

  @InheritInverseConfiguration
  SupplierPreferences toEntity(PublicSupplierPreferencesDto publicSupplierPreferencesDto);

  PublicSupplierPreferencesDto toDto(SupplierPreferences supplierPreferences);
}
