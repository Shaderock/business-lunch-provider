package com.shaderock.lunch.backend.menu.mapper;

import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.model.entity.OptionDescription;
import com.shaderock.lunch.backend.user.order.model.entity.EmployeeOrder;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OptionMapper {

  default Set<Long> employeeOrdersToIds(Set<EmployeeOrder> employeesOrders) {
    return Stream.ofNullable(employeesOrders)
        .flatMap(Collection::stream)
        .map(EmployeeOrder::getId)
        .collect(Collectors.toSet());
  }

  default Set<EmployeeOrder> idsToEmployeeOrders(Set<Long> employeesOrdersIds) {
    return Stream.ofNullable(employeesOrdersIds)
        .flatMap(Collection::stream)
        .map(id -> EmployeeOrder.builder().id(id).build())
        .collect(Collectors.toSet());
  }

  default List<Long> optionDescriptionsToIds(List<OptionDescription> optionDescriptions) {
    return Stream.ofNullable(optionDescriptions)
        .flatMap(Collection::stream)
        .map(OptionDescription::getId)
        .toList();
  }

  default List<OptionDescription> idsToOptionDescriptions(List<Long> optionDescriptionsIds) {
    return Stream.ofNullable(optionDescriptionsIds)
        .flatMap(Collection::stream)
        .map(id -> OptionDescription.builder().id(id).build())
        .toList();
  }

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "employeesOrderIds", source = "employeesOrders")
  @Mapping(target = "optionDescriptionIds", source = "optionDescriptions")
  @Mapping(target = "priceId", source = "price.id")
  OptionDto toDto(Option option);

  @InheritInverseConfiguration
  Option toEntity(OptionDto optionDto);
}
