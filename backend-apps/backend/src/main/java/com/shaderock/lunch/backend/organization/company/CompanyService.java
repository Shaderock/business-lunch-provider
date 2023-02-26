package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.repository.CompanyPreferenceRepository;
import com.shaderock.lunch.backend.organization.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.organization.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import com.shaderock.lunch.backend.utils.FilterManager;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

  private final CompanyPreferenceRepository companyPreferenceRepository;

  private final CompanyRepository companyRepository;
  private final OrganizationDetailsRepository organizationDetailsRepository;
  private final AppUserDetailsService userDetailsService;
  private final OrganizationDetailsService organizationDetailsService;
  private final CompanyPreferencesService companyPreferencesService;
  private final CompanyMapper companyMapper;
  private final FilterManager filterManager;

  @Transactional
  public Company register(OrganizationRegistrationForm form, Principal principal) {
    organizationDetailsService.validateOrganizationRegistration(form, principal);

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name(form.name())
        .build();

    Company persistedCompany = create(organizationDetails);
    userDetails.getAppUser().setOrganizationDetails(persistedCompany.getOrganizationDetails());
    userDetails.getRoles().add(Role.COMPANY_ADMIN);
    userDetails.getRoles().add(Role.EMPLOYEE);

    return persistedCompany;
  }

  @Transactional
  public Company readUserCompany(Principal principal) {
    OrganizationDetails organizationDetails = organizationDetailsService.readByPrincipal(principal);
    return companyRepository.findByOrganizationDetails(organizationDetails)
        .orElseThrow(() -> new CrudValidationException("There is no company assigned"));
  }

  @Transactional
  public Company create(Company company, OrganizationDetails details) {
    if (Objects.isNull(company)) {
      throw new CrudValidationException("Can not create null");
    }

    Company persistedCompany = create(details);
    company.setId(persistedCompany.getId());

    update(company);
    return persistedCompany;
  }

  @Transactional
  public Company create(OrganizationDetails details) {
    OrganizationDetails organizationDetails = organizationDetailsService.create(details);

    Company company = Company.builder()
        .organizationDetails(organizationDetails)
        .build();
    Company persistedCompany = companyRepository.save(company);

    CompanyPreferences preferences = CompanyPreferences.builder().company(persistedCompany).build();
    CompanyPreferences persistedPreferences = companyPreferencesService.create(preferences);
    persistedCompany.setPreferences(persistedPreferences);

    return persistedCompany;
  }

  public List<Company> readAll() {
    return readAll(false);
  }

  public List<Company> readAll(boolean includeDeleted) {
    if (includeDeleted) {
      filterManager.enableDeleteFilter();
    }
    List<Company> all = companyRepository.findAll();
    filterManager.disableDeleteFilter();
    return all;
  }

  public Company read(UUID companyId) {
    return companyRepository.findById(companyId).orElseThrow(() -> new CrudValidationException(
        String.format("Company(id=[%s] not found", companyId)));
  }

  @Transactional
  public Company update(Company company) {
    LOGGER.info("Attempting to update {}", company);
    if (Objects.isNull(company)) {
      throw new CrudValidationException("Can not update null");
    }

    Company persistedCompany = read(company.getId());
    persistedCompany.setOrganizationDetails(company.getOrganizationDetails());
    persistedCompany.setPreferences(company.getPreferences());
    persistedCompany.setSubscriptions(company.getSubscriptions());
    persistedCompany.setSubscriptionsRequests(company.getSubscriptionsRequests());

    LOGGER.info("Updated {}", persistedCompany);
    return persistedCompany;
  }

  @Transactional
  public void delete(Company company) {
    LOGGER.info("Attempting to delete {}", company);
    if (Objects.isNull(company)) {
      throw new CrudValidationException("Can not delete null");
    }

    Company persistedCompany = read(company.getId());
    companyPreferencesService.delete(persistedCompany.getPreferences());
    companyRepository.delete(persistedCompany);
    organizationDetailsService.delete(persistedCompany.getOrganizationDetails());

    LOGGER.info("Deleted {}", persistedCompany);
  }
}
