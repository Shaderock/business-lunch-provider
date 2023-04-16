package com.shaderock.lunch.backend.menu.mapper;

import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Option;
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
public interface CategoryMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "optionIds", source = "options")
  CategoryDto toDto(Category category);

  @InheritInverseConfiguration
  Category toEntity(CategoryDto categoryDto);

  default Set<UUID> optionsToIds(Set<Option> options) {
    return Stream.ofNullable(options)
        .flatMap(Collection::stream)
        .map(Option::getId)
        .collect(Collectors.toSet());
  }

  default Set<Option> idsToOptions(Set<UUID> optionsIds) {
    return Stream.ofNullable(optionsIds)
        .flatMap(Collection::stream)
        .map(id -> Option.builder().id(id).build())
        .collect(Collectors.toSet());
  }
}
