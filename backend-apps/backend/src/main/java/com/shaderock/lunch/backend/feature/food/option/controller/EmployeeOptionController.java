package com.shaderock.lunch.backend.feature.food.option.controller;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.food.option.dto.OptionDto;
import com.shaderock.lunch.backend.feature.food.option.mapper.OptionMapper;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.service.EmployeeOrderService;
import com.shaderock.lunch.backend.util.ApiConstants;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE_OPTION)
@RequiredArgsConstructor
public class EmployeeOptionController {

  private final OptionMapper optionMapper;
  private final EmployeeOrderService employeeOrderService;
  private final AppUserDetailsService userDetailsService;

  @GetMapping
  @Transactional
  public ResponseEntity<List<OptionDto>> readByOrderForDate(Principal principal,
      @RequestParam @NotNull LocalDate date) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    List<EmployeeOrder> employeeOrders = employeeOrderService.read(userDetails, Optional.empty(),
        Optional.of(date));

    return ResponseEntity.ok(employeeOrders.stream()
        .map(EmployeeOrder::getOptions)
        .flatMap(Collection::stream)
        .map(optionMapper::toDto).toList());
  }

}
