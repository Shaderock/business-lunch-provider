package com.shaderock.lunch.backend.organization.supplier.service;


import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SupplierPreferencesCompletedValidator implements Validator {

  public static final String FIELD_REQUIRED_CODE = "field.required";

  @Override
  public boolean supports(Class<?> clazz) {
    return SupplierPreferences.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "requestOffset", FIELD_REQUIRED_CODE,
        "Request offset cannot be null");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryPeriodStartTime",
        FIELD_REQUIRED_CODE,
        "Delivery period start time cannot be null");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryPeriodEndTime", FIELD_REQUIRED_CODE,
        "Delivery period end time cannot be null");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderType", FIELD_REQUIRED_CODE,
        "Order type cannot be null");
  }
}
