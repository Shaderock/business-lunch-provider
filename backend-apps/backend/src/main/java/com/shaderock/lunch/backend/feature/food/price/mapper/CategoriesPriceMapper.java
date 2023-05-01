package com.shaderock.lunch.backend.feature.food.price.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPrice;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPriceDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CategoriesPriceMapper extends BaseMapper {

  @InheritInverseConfiguration
  CategoriesPrice toEntity(CategoriesPriceDto categoriesPriceDto);

  @Mapping(source = "supplierPreferences.id", target = "supplierPreferencesId")
  CategoriesPriceDto toDto(CategoriesPrice categoriesPrice);
}
