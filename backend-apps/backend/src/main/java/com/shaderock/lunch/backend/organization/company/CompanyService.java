package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.company.model.CompanyRegistrationForm;
import com.shaderock.lunch.backend.organization.company.model.dto.CompanyDto;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.organization.company.model.mapper.CompanyMapper;
import com.shaderock.lunch.backend.organization.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.organization.service.OrganizationService;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;

  private final OrganizationRepository organizationRepository;
  private final AppUserDetailsService userDetailsService;
  private final OrganizationService organizationService;
  private final CompanyMapper companyMapper;

  @Transactional
  public Company register(final CompanyRegistrationForm form, Principal principal) {
    validateRegistration(form, principal);

    LOGGER.info("Registering company by [{}]", form);

    Company newCompany = new Company();
    newCompany.setEmail(form.email());
    newCompany.setName(form.name());
    newCompany.setPhone(form.phone());
    newCompany.setDeleted(false);
    newCompany.setUsers(new HashSet<>());

    Company savedCompany = organizationRepository.save(newCompany);

    LOGGER.info("Company registered");

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    userDetails.getRoles().add(Role.COMPANY_ADMIN);
    userDetails.getRoles().add(Role.EMPLOYEE);

    LOGGER.info("User(username=[{}]) roles updated to be [{}]", userDetails.getUsername(),
        userDetails.getRoles());

    AppUser user = userDetails.getAppUser();
    user.setOrganization(savedCompany);

    LOGGER.info("Assigned User(username=[{}]) to company [{}]", userDetails.getUsername(),
        savedCompany.getName());

    savedCompany.getUsers().add(user);

    return savedCompany;
  }

  private void validateRegistration(final CompanyRegistrationForm form, Principal principal) {
    LOGGER.info("Validating company registration with [{}]", form);

    if (organizationRepository.findByName(form.name()).isPresent()) {
      throw new CompanyRegistrationValidationException(
          String.format("OrganizationDetails with name [%s] already exists",
              form.name()));
    }
    if (organizationRepository.findByEmail(form.email()).isPresent()) {
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

    LOGGER.info("Registration is valid");
  }

  @Transactional
  public Company readUserCompany(Principal principal) {
    Organization organization = organizationService.getUserOrganization(principal)
        .orElseThrow(() -> new TransferableApplicationException("Assigned organization not found"));

    return companyRepository.findById(organization.getId())
        .orElseThrow(() -> new TransferableApplicationException(
            "There is an organization assigned, but not a company"));
  }

  public List<CompanyDto> readAllAsDto() {
    List<Company> companies = readAll();
    return companies.stream().map(companyMapper::toDto).toList();
  }

  public List<Company> readAll() {
    List<Company> result = new ArrayList<>();
    for (Company company : companyRepository.findAll()) {
      result.add(company);
    }

    return result;
  }
}
