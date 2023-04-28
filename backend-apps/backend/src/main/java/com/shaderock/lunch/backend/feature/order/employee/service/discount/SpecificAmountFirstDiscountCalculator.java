package com.shaderock.lunch.backend.feature.order.employee.service.discount;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.repository.EmployeeOrderRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpecificAmountFirstDiscountCalculator implements CompanyDiscountTypeCalculator {

  private final EmployeeOrderRepository employeeOrderRepository;

  @Override
  public boolean supports(CompanyDiscountType companyDiscountType) {
    return companyDiscountType == CompanyDiscountType.SPECIFIC_AMOUNT_FIRST;
  }

  @Override
  public double calculate(@NonNull EmployeeOrder employeeOrder,
      @NonNull CompanyPreferences companyPreferences) {
    List<EmployeeOrder> ordersForSameDay = employeeOrderRepository.findByAppUserAndOrderDate(
        employeeOrder.getAppUser(), employeeOrder.getOrderDate());

    if (!ordersForSameDay.isEmpty()) {
      return 0;
    }

    double discountFix = companyPreferences.getDiscountFixFirstOrder();
    double supplierDefaultPrice = employeeOrder.getSupplierDefaultPrice();
    double supplierDiscount = employeeOrder.getSupplierDiscount();

    return Math.min(discountFix, supplierDefaultPrice - supplierDiscount);
  }
}
