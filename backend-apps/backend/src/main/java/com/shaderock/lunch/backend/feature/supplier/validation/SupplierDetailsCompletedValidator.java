package com.shaderock.lunch.backend.feature.supplier.validation;

import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SupplierDetailsCompletedValidator implements Validator {

  public static final String FIELD_REQUIRED_CODE = "field.required";

  @Override
  public boolean supports(Class<?> clazz) {
    return OrganizationDetails.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", FIELD_REQUIRED_CODE,
        "Description cannot be null");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", FIELD_REQUIRED_CODE,
        "Email cannot be null");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", FIELD_REQUIRED_CODE,
        "Phone cannot be null");
  }

}
