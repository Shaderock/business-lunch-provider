package com.shaderock.lunch.backend.feature.order.employee.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.order.employee.dto.EmployeeOrderDto;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.mapper.EmployeeOrderMapper;
import com.shaderock.lunch.backend.feature.order.employee.repository.EmployeeOrderRepository;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeOrderService {

  private final EmployeeOrderValidationService orderValidationService;
  private final EmployeeOrderRepository orderRepository;
  private final EmployeeOrderMapper orderMapper;
  private final OptionService optionService;

  public EmployeeOrder create(@NonNull EmployeeOrderDto orderDto,
      @NonNull AppUserDetails userDetails) {
    orderValidationService.validateCreate(orderDto, userDetails);
    EmployeeOrder order = orderMapper.toEntity(orderDto);
    order.setAppUser(userDetails.getAppUser());
    order.setOptions(order.getOptions().stream()
        .map(option -> optionService.read(option.getId()))
        .toList());
    order.setStatus(EmployeeOrderStatus.PENDING_ADMIN_CONFIRMATION);
    order.setSupplierDefaultPrice(calculateDefaultPrice(order.getOptions()));
    order.setSupplierDiscount(5); // todo

    // todo calculate final price correctly, consider to correctly calculate discounts
    double finalPrice =
        order.getSupplierDefaultPrice() - order.getSupplierDiscount() - order.getCompanyDiscount();
    if (finalPrice < 0) {
      finalPrice = 0;
    }
    order.setFinalPrice(finalPrice);

    return orderRepository.save(order);
  }

  // todo
  private double calculateDefaultPrice(Collection<Option> options) {
    return options.stream()
        .mapToDouble(Option::getPrice)
        .sum();
  }

  public List<EmployeeOrder> read(@NonNull AppUserDetails userDetails,
      @NonNull Optional<EmployeeOrderStatus> status, @NonNull Optional<LocalDate> date) {
    if (status.isEmpty() && date.isEmpty()) {
      return orderRepository.findByAppUser_UserDetails(userDetails);
    }

    if (status.isEmpty()) {
      return orderRepository.findByAppUser_UserDetailsAndOrderDate(userDetails, date.get());
    }

    if (date.isEmpty()) {
      return orderRepository.findByAppUser_UserDetailsAndStatus(userDetails, status.get());
    }

    return orderRepository.findByAppUser_UserDetailsAndStatusAndOrderDate(userDetails, status.get(),
        date.get());
  }

  public EmployeeOrder read(@NonNull UUID orderId, @NonNull AppUserDetails userDetails) {
    return orderRepository.findByIdAndAppUser_UserDetails(orderId, userDetails).orElseThrow(() ->
        new CrudValidationException(
            String.format("Could not find order(id=[%s) for user(email=[%s])", orderId,
                userDetails.getEmail())));
  }

  public void delete(@NonNull EmployeeOrder order) {
    orderValidationService.validateDelete(order);
    orderRepository.delete(order);
  }
}
