package com.shaderock.backend.company;

import com.shaderock.backend.company.error.exception.CompanyRegistrationValidationException;
import com.shaderock.backend.company.model.CompanyDTO;
import com.shaderock.backend.company.model.CompanyRegistrationForm;
import com.shaderock.backend.model.entity.company.Company;
import com.shaderock.backend.model.entity.user.AppUser;
import com.shaderock.backend.model.entity.user.Employee;
import com.shaderock.backend.model.type.Role;
import com.shaderock.backend.repository.user.AppUserRepository;
import com.shaderock.backend.repository.user.CompanyRepository;
import com.shaderock.backend.service.user.AppUserConverter;
import com.shaderock.backend.service.user.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final EmployeeService employeeService;
  private final AppUserRepository<AppUser> appUserRepository;
  private final AppUserConverter<AppUser, Employee> appUserConverter;

  @Transactional
  public Company register(final CompanyRegistrationForm form, Principal principal) {
    validateRegistration(form, principal);

    Company newCompany = Company.builder()
            .email(form.getEmail())
            .name(form.getName())
            .phone(form.getPhone())
            .employees(new HashSet<>())
            .isDeleted(false)
            .build();
    Company savedCompany = companyRepository.save(newCompany);

    AppUser appUser = appUserRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=[%s} not found", principal.getName())));

    Employee employee = new Employee();
    appUserConverter.convertFromTo(appUser, employee);
    employee.setCompany(savedCompany);
    employee.getRoles().add(Role.COMPANY_ADMIN);
    employee.getRoles().add(Role.EMPLOYEE);
    employee = employeeService.save(employee);

    savedCompany.getEmployees().add(employee);
    savedCompany = companyRepository.save(newCompany);

    return savedCompany;
  }

  private void validateRegistration(final CompanyRegistrationForm form, Principal principal) {
    if (companyRepository.findByName(form.getName()).isPresent()) {
      throw new CompanyRegistrationValidationException(String.format("Company with name [%s] already exists",
                                                                     form.getName()));
    }
    if (companyRepository.findByEmail(form.getEmail()).isPresent()) {
      throw new CompanyRegistrationValidationException(String.format("Company with email [%s] already exists",
                                                                     form.getEmail()));
    }

    AppUser appUser = appUserRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=[%s} not found", principal.getName())));

    if (appUser.getRoles().size() > 1 && !appUser.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(String.format("User [%s] can not create companies",
                                                                     form.getEmail()));
    }
  }

  public CompanyDTO convertToDto(Company company) {
    return CompanyDTO.builder()
            .name(company.getName())
            .email(company.getEmail())
            .phone(company.getPhone())
            .build();
  }
}
