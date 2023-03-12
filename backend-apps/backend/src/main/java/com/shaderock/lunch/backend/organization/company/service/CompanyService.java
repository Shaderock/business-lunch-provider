package com.shaderock.lunch.backend.organization.company.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import com.shaderock.lunch.backend.utils.FilterManager;
import jakarta.transaction.Transactional;
import java.util.List;
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
  private final CompanyPreferencesServiceImpl companyPreferencesService;
  private final FilterManager filterManager;

  @Transactional
  public Company register(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    organizationDetailsService.validateOrganizationRegistration(form, userDetails);

    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name(form.name())
        .build();

    Company persistedCompany = create(organizationDetails, userDetails);

    return persistedCompany;
  }

  @Transactional
  public Company read(@NonNull AppUserDetails userDetails) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(userDetails);
    return companyRepository.findByOrganizationDetails(organizationDetails)
        .orElseThrow(() -> new CrudValidationException("There is no company assigned"));
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

  public List<Company> readAll() {
    return companyRepository.findAll();
  }

  public List<Company> readAllDeleted() {
    filterManager.enableDeleteFilter();
    List<Company> all = readAll();
    filterManager.disableDeleteFilter();
    return all;
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
    persistedCompany.setSubscriptionsRequests(company.getSubscriptionsRequests());

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
}
