package com.shaderock.lunch.backend.feature.company.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
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
@Slf4j
@RequiredArgsConstructor
public class CompanyValidationService {

  private final CompanyDetailsCompletedValidator companyDetailsCompletedValidator;
  private final CompanyPreferencesCompletedValidator companyPreferencesCompletedValidator;

  public void validateCompanyProfileIsCompleted(@NonNull Company company) {
    CompanyPreferences preferences = company.getPreferences();
    Errors preferencesErrors = new BindException(preferences, "supplierPreferences");
    companyPreferencesCompletedValidator.validate(preferences, preferencesErrors);

    OrganizationDetails details = company.getOrganizationDetails();
    Errors detailsErrors = new BindException(details, "supplierDetails");
    companyDetailsCompletedValidator.validate(details, detailsErrors);

    List<String> errors = new ArrayList<>();
    if (preferencesErrors.hasErrors() || detailsErrors.hasErrors()) {

      for (FieldError fieldError : detailsErrors.getFieldErrors()) {
        errors.add(fieldError.getDefaultMessage());
      }

      for (FieldError fieldError : preferencesErrors.getFieldErrors()) {
        errors.add(fieldError.getDefaultMessage());
      }

      throw new CrudValidationException("Company profile is not completed", errors);
    }
  }

  public void validateRemoveEmployee(@NonNull AppUser appUser, @NonNull Company company) {
    if (appUser.getUserDetails().getRoles().contains(Role.COMPANY_ADMIN)) {
      long adminsCount = countCompanyAdmins(appUser, company);
      if (adminsCount == 1) {
        throw new CrudValidationException("Can not remove last company admin from the company");
      }
    }
  }

  public void validateUserIsEmployee(@NonNull AppUserDetails userDetails) {
    if (!userDetails.getRoles().contains(Role.EMPLOYEE)) {
      throw new CrudValidationException("User is not a part of a company");
    }
  }

  public void validateRevokeAdminRole(@NonNull AppUser appUser, @NonNull Company company) {
    long companyAdminsAmount = countCompanyAdmins(appUser, company);
    if (companyAdminsAmount == 1) {
      throw new CrudValidationException("Can not revoke admin rights from the last company admin");
    }
  }

  private long countCompanyAdmins(AppUser appUser, Company company) {
    return company.getOrganizationDetails().getUsers().stream()
        .map(AppUser::getUserDetails)
        .filter(userDetails -> appUser.getUserDetails().getRoles().contains(Role.COMPANY_ADMIN))
        .count();
  }
}
