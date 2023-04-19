package com.shaderock.lunch.backend.feature.supplier.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.supplier.dto.PublicSupplierDto;
import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SupplierMapper extends BaseMapper {

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



}
