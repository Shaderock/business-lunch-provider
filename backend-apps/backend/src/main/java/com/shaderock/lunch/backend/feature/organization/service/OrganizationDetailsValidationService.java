package com.shaderock.lunch.backend.feature.organization.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationDetailsValidationService {

  private final OrganizationDetailsRepository organizationDetailsRepository;

  public void validateOrganizationRegistration(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    LOGGER.info("Validating organization registration by [{}]", form);

    if (Objects.isNull(userDetails.getRoles())) {
      throw new CompanyRegistrationValidationException(
          "Can not register an organization for a user without roles");
    }

    if (userDetails.getRoles().size() > 1 || !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(
          String.format("User(email=[%s]) can not register organizations", userDetails.getEmail()));
    }

    LOGGER.info("Organization is valid for registration");
  }

  public void validateCreate(@NonNull OrganizationDetails details) {
    if (StringUtils.isBlank(details.getName())) {
      throw new CrudValidationException("Can not create organization without name");
    }

    if (organizationDetailsRepository.findByName(details.getName()).isPresent()) {
      throw new CrudValidationException(String.format("Organization(name=[%s]) already exists",
          details.getName()));
    }
  }

  public void validateUpdate(@NonNull OrganizationDetails details) {
    if (StringUtils.isBlank(details.getName())) {
      throw new CrudValidationException("Can not update organization without name");
    }
  }

  public void validateRemoveUser(@NonNull AppUser appUser,
      @NonNull OrganizationDetails organizationDetails) {
    if (organizationDetails.getUsers().size() == 1) {
      throw new CrudValidationException("Can not remove last user from the organization");
    }

    validateUserIsPartOfOrganization(appUser, organizationDetails);
  }

  public void validateUserIsPartOfOrganization(AppUser appUser,
      OrganizationDetails organizationDetails) {
    if (!organizationDetails.getUsers().contains(appUser)) {
      throw new CrudValidationException("User is not a part of organization");
    }
  }
}
