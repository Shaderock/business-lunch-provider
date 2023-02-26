package com.shaderock.lunch.backend.menu.model.mapper;

import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CategoryMapper {

  @Mapping(target = "optionIds", source = "options")
  @Mapping(target = "menuId", source = "menu.id")
  CategoryDto toDto(Category category);

  @InheritInverseConfiguration
  Category toEntity(CategoryDto categoryDto);

  default Set<Long> optionsToIds(Set<Option> options) {
    return options.stream()
        .map(Option::getId)
        .collect(Collectors.toSet());
  }

  default Set<Option> idsToOptions(Set<Long> optionsIds) {
    return optionsIds.stream()
        .map(id -> Option.builder().id(id).build())
        .collect(Collectors.toSet());
  }
}
