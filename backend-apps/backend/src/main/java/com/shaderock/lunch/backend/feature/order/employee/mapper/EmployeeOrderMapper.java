package com.shaderock.lunch.backend.feature.order.employee.mapper;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface EmployeeOrderMapper extends BaseMapper {

  @Mapping(target = "userDetailsId", source = "appUser.userDetails.id")
  @Mapping(target = "companyOrderId", source = "companyOrder.id")
  @Mapping(target = "optionIds", source = "options")
  EmployeeOrderDto toDto(EmployeeOrder employeeOrder);

  @InheritInverseConfiguration
  @Mapping(source = "optionIds", target = "options")
  EmployeeOrder toEntity(EmployeeOrderDto employeeOrderDto);
}
