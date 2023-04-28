package com.shaderock.lunch.backend.feature.order.employee.service.discount;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import lombok.NonNull;

public interface CompanyDiscountTypeCalculator {

  boolean supports(CompanyDiscountType companyDiscountType);

  double calculate(@NonNull EmployeeOrder employeeOrder,
      @NonNull CompanyPreferences companyPreferences);
}
