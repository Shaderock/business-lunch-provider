package com.shaderock.lunch.backend.feature.order.company.entity;

import com.shaderock.lunch.backend.data.mapper.BaseMapper;
import com.shaderock.lunch.backend.feature.order.company.dto.CompanyOrderDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyOrderMapper extends BaseMapper {

  @InheritInverseConfiguration
  @Mapping(target = "employeesOrders", source = "employeesOrderIds")
  CompanyOrder toEntity(CompanyOrderDto companyOrderDto);

  @Mapping(target = "companyName", source = "employeesOrders")
  @Mapping(target = "employeesOrderIds", source = "employeesOrders")
  CompanyOrderDto toDto(CompanyOrder companyOrder);
}
