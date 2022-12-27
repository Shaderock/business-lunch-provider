package com.shaderock.backend.organization.company;

import com.shaderock.backend.organization.company.model.CompanyDTO;
import com.shaderock.backend.organization.company.model.CompanyRegistrationForm;
import com.shaderock.backend.organization.company.model.entity.Company;
import com.shaderock.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.backend.user.AppUserDetailsService;
import com.shaderock.backend.user.model.entity.AppUser;
import com.shaderock.backend.user.model.entity.AppUserDetails;
import com.shaderock.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final AppUserDetailsService userDetailsService;

  @Transactional
  public Company register(final CompanyRegistrationForm form, Principal principal) {
    validateRegistration(form, principal);

    Company newCompany = new Company();
    newCompany.setEmail(form.email());
    newCompany.setName(form.name());
    newCompany.setPhone(form.phone());
    newCompany.setDeleted(false);
    newCompany.setUsers(new HashSet<>());

    Company savedCompany = companyRepository.save(newCompany);

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    userDetails.getRoles().add(Role.COMPANY_ADMIN);
    userDetails.getRoles().add(Role.EMPLOYEE);

    AppUser user = userDetails.getAppUser();
    user.setOrganization(savedCompany);

    savedCompany.getUsers().add(user);

    return savedCompany;
  }

  private void validateRegistration(final CompanyRegistrationForm form, Principal principal) {
    if (companyRepository.findByName(form.name()).isPresent()) {
      throw new CompanyRegistrationValidationException(String.format("Company with name [%s] already exists",
                                                                     form.name()));
    }
    if (companyRepository.findByEmailAndDeletedIsFalse(form.email()).isPresent()) {
      throw new CompanyRegistrationValidationException(String.format("Company with email [%s] already exists",
                                                                     form.email()));
    }

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

    if (userDetails.getRoles().size() > 1 && !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(String.format("User [%s] can not create companies",
                                                                     form.email()));
    }
  }

  @Transactional
  public CompanyDTO convertToDto(Company company) {
    return new CompanyDTO(company.getName(), company.getEmail(), company.getPhone());
  }
}
