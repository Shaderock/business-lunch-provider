package com.shaderock.lunch.backend.organization.company.model.mapper;

import com.shaderock.lunch.backend.organization.company.model.dto.CompanyDto;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyMapper {

  @Mapping(target = "usersIds", source = "users")
  @Mapping(target = "usersRequestsIds", source = "usersRequests")
  @Mapping(target = "subscriptionsIds", source = "subscriptions")
  @Mapping(target = "subscriptionsRequestsIds", source = "subscriptionsRequest")
  @Mapping(target = "preferencesId", source = "preferences.id")
  CompanyDto toDto(Company company);

  @InheritInverseConfiguration
  Company toEntity(CompanyDto companyDto);

  default Set<Long> usersToIds(Set<AppUser> users) {
    return Stream.ofNullable(users)
        .flatMap(Collection::stream)
        .map(AppUser::getId)
        .collect(Collectors.toSet());
  }

  default Set<Long> subscriptionsToIds(Set<Supplier> subscriptions) {
    return Stream.ofNullable(subscriptions)
        .flatMap(Collection::stream)
        .map(Supplier::getId)
        .collect(Collectors.toSet());
  }

  default Set<Supplier> idsToSubscriptions(Set<Long> subscriptionsIds) {
    return Stream.ofNullable(subscriptionsIds)
        .flatMap(Collection::stream)
        .map(id -> {
          Supplier supplier = new Supplier();
          supplier.setId(id);
          return supplier;
        })
        .collect(Collectors.toSet());
  }
}
