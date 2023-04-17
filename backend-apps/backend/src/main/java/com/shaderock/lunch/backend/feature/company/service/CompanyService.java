package com.shaderock.lunch.backend.feature.company.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.service.CompanyPreferencesServiceImpl;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsValidationService;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.type.Role;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
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
  private final OrganizationDetailsValidationService organizationDetailsValidationService;

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
