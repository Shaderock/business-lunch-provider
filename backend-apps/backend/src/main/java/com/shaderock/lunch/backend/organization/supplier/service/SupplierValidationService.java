package com.shaderock.lunch.backend.organization.supplier.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierValidationService {

  private final SupplierDetailsCompletedValidator supplierDetailsCompletedValidator;
  private final SupplierPreferencesCompletedValidator supplierPreferencesCompletedValidator;


  public void validateCanCreatePublicCategories(@NonNull Supplier supplier) {
    validateSupplierProfileIsCompleted(supplier);
  }

  public void validateCanCreatePublicOptions(@NonNull Supplier supplier) {
    validateSupplierProfileIsCompleted(supplier);
  }

  public void validateSupplierProfileIsCompleted(@NonNull Supplier supplier) {
    SupplierPreferences preferences = supplier.getPreferences();
    Errors preferencesErrors = new BindException(preferences, "supplierPreferences");
    supplierPreferencesCompletedValidator.validate(preferences, preferencesErrors);

    OrganizationDetails details = supplier.getOrganizationDetails();
    Errors detailsErrors = new BindException(details, "supplierDetails");
    supplierDetailsCompletedValidator.validate(details, detailsErrors);

    List<String> errors = new ArrayList<>();
    if (preferencesErrors.hasErrors() || detailsErrors.hasErrors()) {

      for (FieldError fieldError : detailsErrors.getFieldErrors()) {
        errors.add(fieldError.getDefaultMessage());
      }

      for (FieldError fieldError : preferencesErrors.getFieldErrors()) {
        errors.add(fieldError.getDefaultMessage());
      }

      throw new CrudValidationException("Supplier profile is not completed", errors);
    }
  }
}
