package com.shaderock.lunch.backend.feature.company.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsValidationService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final OrganizationDetailsService organizationDetailsService;
  private final CompanyPreferencesService companyPreferencesService;
  private final OrganizationDetailsValidationService organizationDetailsValidationService;
  private final CompanyValidationService companyValidationService;
  private final AppUserDetailsService userDetailsService;

  @Transactional(TxType.REQUIRES_NEW)
  public Company register(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    organizationDetailsValidationService.validateOrganizationRegistration(form, userDetails);

    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name(form.name())
        .build();

    return create(organizationDetails, userDetails);
  }

  @Transactional
  public Company create(@NonNull Company company, @NonNull OrganizationDetails organizationDetails,
      AppUserDetails userDetails) {
    Company persistedCompany = create(organizationDetails, userDetails);
    company.setId(persistedCompany.getId());

    update(company);
    return persistedCompany;
  }

  @Transactional
  public Company create(@NonNull OrganizationDetails details, @NonNull AppUserDetails userDetails) {
    OrganizationDetails organizationDetails = organizationDetailsService.create(details);

    Company company = Company.builder()
        .organizationDetails(organizationDetails)
        .build();
    Company persistedCompany = companyRepository.save(company);

    CompanyPreferences preferences = CompanyPreferences.builder().company(persistedCompany).build();
    CompanyPreferences persistedPreferences = companyPreferencesService.create(preferences);
    persistedCompany.setPreferences(persistedPreferences);

    userDetails.getAppUser().setOrganizationDetails(persistedCompany.getOrganizationDetails());
    userDetails.getRoles().add(Role.COMPANY_ADMIN);
    userDetails.getRoles().add(Role.EMPLOYEE);

    return persistedCompany;
  }

  public Company read(@NonNull Principal principal) {
    return read(principal.getName());
  }

  public Company read(@NonNull String userEmail) {
    return read(userDetailsService.loadUserByUsername(userEmail));
  }

  public Company read(@NonNull AppUserDetails userDetails) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(userDetails);
    return companyRepository.findByOrganizationDetails(organizationDetails)
        .orElseThrow(() -> new CrudValidationException("There is no company assigned"));
  }

  public List<Company> read() {
    return companyRepository.findAll();
  }

  public Company read(@NonNull UUID companyId) {
    return companyRepository.findById(companyId).orElseThrow(() -> new CrudValidationException(
        String.format("Company(id=[%s] not found", companyId)));
  }


  @Transactional
  public Company update(@NonNull Company company) {
    LOGGER.info("Attempting to update {}", company);

    Company persistedCompany = read(company.getId());
    persistedCompany.setSubscriptions(company.getSubscriptions());

    LOGGER.info("Updated {}", persistedCompany);
    return persistedCompany;
  }

  @Transactional
  public void delete(@NonNull UUID id) {
    LOGGER.info("Attempting to delete company by id=[{}]", id);

    Company persistedCompany = read(id);
    companyPreferencesService.delete(persistedCompany.getPreferences());
    companyRepository.delete(persistedCompany);
    organizationDetailsService.delete(persistedCompany.getOrganizationDetails());

    LOGGER.info("Deleted {}", persistedCompany);
  }

  @Transactional
  public void addEmployee(@NonNull AppUser appUser, @NonNull Company company) {
    appUser.getUserDetails().getRoles().add(Role.EMPLOYEE);
    organizationDetailsService.addUser(appUser, company.getOrganizationDetails());
  }

  @Transactional
  public void removeEmployee(@NonNull AppUser appUser, @NonNull Company company) {
    organizationDetailsService.removeUser(appUser, company.getOrganizationDetails());
    companyValidationService.validateRemoveEmployee(appUser, company);
    appUser.getUserDetails().getRoles().removeAll(Set.of(Role.EMPLOYEE, Role.COMPANY_ADMIN));
  }

  @Transactional
  public void grantAdminRole(AppUser appUser, Company company) {
    organizationDetailsValidationService.validateUserIsPartOfOrganization(appUser,
        company.getOrganizationDetails());
    appUser.getUserDetails().getRoles().add(Role.COMPANY_ADMIN);
  }

  @Transactional
  public void revokeAdminRole(AppUser appUser, Company company) {
    organizationDetailsValidationService.validateUserIsPartOfOrganization(appUser,
        company.getOrganizationDetails());
    companyValidationService.validateRevokeAdminRole(appUser, company);
    appUser.getUserDetails().getRoles().remove(Role.COMPANY_ADMIN);
  }
}
