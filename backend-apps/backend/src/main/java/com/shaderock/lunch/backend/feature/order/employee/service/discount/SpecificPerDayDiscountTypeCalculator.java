package com.shaderock.lunch.backend.feature.order.employee.service.discount;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.repository.EmployeeOrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpecificPerDayDiscountTypeCalculator implements CompanyDiscountTypeCalculator {

  private final EmployeeOrderRepository employeeOrderRepository;

  @Override
  public boolean supports(CompanyDiscountType companyDiscountType) {
    return CompanyDiscountType.SPECIFIC_PER_DAY == companyDiscountType;
  }

  @Override
  public double calculate(@NonNull EmployeeOrder employeeOrder,
      @NonNull CompanyPreferences companyPreferences) {
    double sumOfCompanyDiscountsForOrderDate = employeeOrderRepository
        .findByAppUserAndOrderDate(employeeOrder.getAppUser(), employeeOrder.getOrderDate())
        .stream()
        .mapToDouble(EmployeeOrder::getCompanyDiscount)
        .sum();

    double discountPerDay = companyPreferences.getDiscountPerDay();
    double availableDiscount = discountPerDay - sumOfCompanyDiscountsForOrderDate;
    double supplierDefaultPrice = employeeOrder.getSupplierDefaultPrice();
    double supplierDiscount = employeeOrder.getSupplierDiscount();

    return Math.min(availableDiscount, supplierDefaultPrice - supplierDiscount);
  }
}
