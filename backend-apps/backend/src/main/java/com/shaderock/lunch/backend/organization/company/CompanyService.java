package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.OrganizationService;
import com.shaderock.lunch.backend.organization.company.model.CompanyRegistrationForm;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository<Company> companyRepository;
  private final OrganizationRepository<Organization> organizationRepository;
  private final AppUserDetailsService userDetailsService;
  private final OrganizationService organizationService;

  @Transactional
  public Company register(final CompanyRegistrationForm form, Principal principal) {
    validateRegistration(form, principal);

    log.info("Registering company by [{}]", form);

    Company newCompany = new Company();
    newCompany.setEmail(form.email());
    newCompany.setName(form.name());
    newCompany.setPhone(form.phone());
    newCompany.setDeleted(false);
    newCompany.setUsers(new HashSet<>());

    Company savedCompany = companyRepository.save(newCompany);

    log.info("Company registered");

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    userDetails.getRoles().add(Role.COMPANY_ADMIN);
    userDetails.getRoles().add(Role.EMPLOYEE);

    log.info("User(username=[{}]) roles updated to be [{}]", userDetails.getUsername(),
        userDetails.getRoles());

    AppUser user = userDetails.getAppUser();
    user.setOrganization(savedCompany);

    log.info("Assigned User(username=[{}]) to company [{}]", userDetails.getUsername(),
        savedCompany.getName());

    savedCompany.getUsers().add(user);

    return savedCompany;
  }

  private void validateRegistration(final CompanyRegistrationForm form, Principal principal) {
    log.info("Validating company registration with [{}]", form);

    if (organizationRepository.findByName(form.name()).isPresent()) {
      throw new CompanyRegistrationValidationException(
          String.format("Organization with name [%s] already exists",
              form.name()));
    }
    if (companyRepository.findByEmail(form.email()).isPresent()) {
      throw new CompanyRegistrationValidationException(
          String.format("Company with email [%s] already exists",
              form.email()));
    }

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

    if (userDetails.getRoles().size() > 1 && !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(
          String.format("User [%s] can not create companies",
              form.email()));
    }

    log.info("Registration is valid");
  }

  @Transactional
  public Company getUserCompany(Principal principal) {
    Organization organization = organizationService.getUserOrganization(principal);
    return companyRepository.findByName(organization.getName())
        .orElseThrow(() -> new TransferableApplicationException("Assigned company not found"));
  }
}
