package com.shaderock.lunch.backend.feature.food.option.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.food.option.dto.OptionDto;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OptionMapper extends BaseMapper {


  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "employeesOrderIds", source = "employeesOrders")
  @Mapping(target = "subOptionsIds", source = "subOptions")
  OptionDto toDto(Option option);

  @InheritInverseConfiguration
  Option toEntity(OptionDto optionDto);
}
