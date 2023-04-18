package com.shaderock.lunch.backend.feature.company.service;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CompanyPreferencesCompletedValidator implements Validator {

  public static final String FIELD_REQUIRED_CODE = "field.required";

  @Override
  public boolean supports(Class<?> clazz) {
    return CompanyPreferences.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryAddress", FIELD_REQUIRED_CODE,
        "Delivery address can not be empty");
  }
}
