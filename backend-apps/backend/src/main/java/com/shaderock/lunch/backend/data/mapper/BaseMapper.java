package com.shaderock.lunch.backend.data.mapper;

import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.price.entity.PriceForCategories;
import com.shaderock.lunch.backend.feature.food.suboption.entity.SubOption;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface BaseMapper {

  default List<UUID> notificationsToIds(List<Notification> notifications) {
    return Stream.ofNullable(notifications)
        .flatMap(Collection::stream)
        .map(Notification::getId)
        .toList();
  }

  default List<Notification> idsToNotifications(List<UUID> invitationsIds) {
    return Stream.ofNullable(invitationsIds)
        .flatMap(Collection::stream)
        .map(id -> Notification.builder().id(id).build())
        .toList();
  }

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

  default Set<UUID> subscriptionsToIds(Set<Subscription> subscriptions) {
    return Stream.ofNullable(subscriptions)
        .flatMap(Collection::stream)
        .map(Subscription::getId)
        .collect(Collectors.toSet());
  }

  default Set<Subscription> idsToSubscriptions(Set<UUID> ids) {
    return Stream.ofNullable(ids)
        .flatMap(Collection::stream)
        .map(id -> Subscription.baseEntityBuilder().id(id).build())
        .collect(Collectors.toSet());
  }

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

  default List<UUID> optionDescriptionsToIds(List<SubOption> subOptions) {
    return Stream.ofNullable(subOptions)
        .flatMap(Collection::stream)
        .map(SubOption::getId)
        .toList();
  }

  default List<SubOption> idsToOptionDescriptions(List<UUID> subOptionsIds) {
    return Stream.ofNullable(subOptionsIds)
        .flatMap(Collection::stream)
        .map(id -> SubOption.builder().id(id).build())
        .toList();
  }


  default List<UUID> usersToIds(List<AppUser> users) {
    return Stream.ofNullable(users)
        .flatMap(Collection::stream)
        .map(AppUser::getId)
        .toList();
  }

  default List<AppUser> idsToUsers(List<UUID> usersIds) {
    return Stream.ofNullable(usersIds)
        .flatMap(Collection::stream)
        .map(id -> AppUser.baseEntityBuilder().id(id).build())
        .toList();
  }

  default Set<UUID> priceForCategoriesToIds(Set<PriceForCategories> priceForCategories) {
    return Stream.ofNullable(priceForCategories)
        .flatMap(Collection::stream)
        .map(PriceForCategories::getId)
        .collect(Collectors.toSet());
  }

  default Set<PriceForCategories> idsToPriceForCategories(Set<UUID> categoriesIds) {
    return Stream.ofNullable(categoriesIds)
        .flatMap(Collection::stream)
        .map(id -> PriceForCategories.builder().id(id).build())
        .collect(Collectors.toSet());
  }
}