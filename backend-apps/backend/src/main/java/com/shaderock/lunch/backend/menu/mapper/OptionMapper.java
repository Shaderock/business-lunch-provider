package com.shaderock.lunch.backend.menu.mapper;

import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.model.entity.SubOptions;
import com.shaderock.lunch.backend.user.order.model.entity.EmployeeOrder;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OptionMapper {

  default Set<UUID> employeeOrdersToIds(Set<EmployeeOrder> employeesOrders) {
    return Stream.ofNullable(employeesOrders)
        .flatMap(Collection::stream)
        .map(EmployeeOrder::getId)
        .collect(Collectors.toSet());
  }

  default Set<EmployeeOrder> idsToEmployeeOrders(Set<UUID> employeesOrdersIds) {
    return Stream.ofNullable(employeesOrdersIds)
        .flatMap(Collection::stream)
        .map(id -> EmployeeOrder.builder().id(id).build())
        .collect(Collectors.toSet());
  }

  default List<UUID> optionDescriptionsToIds(List<SubOptions> subOptions) {
    return Stream.ofNullable(subOptions)
        .flatMap(Collection::stream)
        .map(SubOptions::getId)
        .toList();
  }

  default List<SubOptions> idsToOptionDescriptions(List<UUID> subOptionsIds) {
    return Stream.ofNullable(subOptionsIds)
        .flatMap(Collection::stream)
        .map(id -> SubOptions.builder().id(id).build())
        .toList();
  }

  @Mapping(target = "isPublic", source = "public")
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "employeesOrderIds", source = "employeesOrders")
  @Mapping(target = "subOptionsIds", source = "subOptions")
  OptionDto toDto(Option option);

  @InheritInverseConfiguration
  Option toEntity(OptionDto optionDto);
}
