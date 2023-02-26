package com.shaderock.lunch.backend.organization.supplier.mapper;

import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
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
public interface SupplierMapper {

  @Mapping(target = "usersIds", source = "users")
  @Mapping(target = "usersRequestsIds", source = "usersRequests")
  @Mapping(target = "subscribersIds", source = "subscribers")
  @Mapping(target = "subscriptionsRequestsIds", source = "subscriptionsRequests")
  @Mapping(target = "menuId", source = "menu.id")
  @Mapping(target = "preferencesId", source = "preferences.id")
  SupplierDto toDto(Supplier supplier);

  @InheritInverseConfiguration
  Supplier toEntity(SupplierDto supplierDto);

  default Set<Long> usersToIds(Set<AppUser> users) {
    return Stream.ofNullable(users)
        .flatMap(Collection::stream)
        .map(AppUser::getId)
        .collect(Collectors.toSet());
  }

  default Set<AppUser> idsToUsers(Set<Long> usersIds) {
    return Stream.ofNullable(usersIds)
        .flatMap(Collection::stream)
        .map(id -> AppUser.builder().id(id).build())
        .collect(Collectors.toSet());
  }

  default Set<Long> subscribersToIds(Set<Company> subscribers) {
    return Stream.ofNullable(subscribers)
        .flatMap(Collection::stream)
        .map(Company::getId)
        .collect(Collectors.toSet());
  }

  default Set<Company> idsToSubscribers(Set<Long> subscribersIds) {
    return Stream.ofNullable(subscribersIds)
        .flatMap(Collection::stream)
        .map(id -> {
          Company company = new Company();
          company.setId(id);
          return company;
        })
        .collect(Collectors.toSet());
  }
}
