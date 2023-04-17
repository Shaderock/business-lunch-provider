package com.shaderock.lunch.backend.feature.subscription.mapper;

import com.shaderock.lunch.backend.feature.subscription.dto.SubscriptionDto;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SubscriptionMapper {

  @Mapping(target = "supplier", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(source = "supplierId", target = "supplier.id")
  @Mapping(source = "companyId", target = "company.id")
  Subscription toEntity(SubscriptionDto subscriptionDto);

  @InheritInverseConfiguration
  SubscriptionDto toDto(Subscription subscription);
}
