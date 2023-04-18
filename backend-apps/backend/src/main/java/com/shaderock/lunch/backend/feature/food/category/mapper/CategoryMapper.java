package com.shaderock.lunch.backend.feature.food.category.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CategoryMapper extends BaseMapper {

  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "optionIds", source = "options")
  CategoryDto toDto(Category category);

  @InheritInverseConfiguration
  Category toEntity(CategoryDto categoryDto);

}
