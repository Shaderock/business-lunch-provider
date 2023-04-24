package com.shaderock.lunch.backend.feature.supplier.validation;


import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
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
        "Request offset can not be empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workDayStart",
        FIELD_REQUIRED_CODE,
        "Work start time can not be empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workDayEnd", FIELD_REQUIRED_CODE,
        "Work end time can not be empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderType", FIELD_REQUIRED_CODE,
        "Order type can not be empty");
  }
}
