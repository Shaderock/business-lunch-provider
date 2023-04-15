package com.shaderock.lunch.backend.organization.company.service;


import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.repository.CompanyRepository;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTests {

  @Mock
  private CompanyRepository companyRepository;
  @Mock
  private OrganizationDetailsService organizationDetailsService;
  @Mock
  private CompanyPreferencesServiceImpl companyPreferencesService;
  @InjectMocks
  private CompanyService companyService;
  private OrganizationDetails organizationDetails;
  private Company company;
  private AppUserDetails userDetails;
  private OrganizationRegistrationForm organizationRegistrationForm;
  private CompanyPreferences companyPreferences;

  @BeforeEach
  public void init() {
    organizationDetails = OrganizationDetails.builder()
        .id(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"))
        .name("organization")
        .phone("+373777777")
        .email("organization@dummy.email.test")
        .build();

    company = Company.builder()
        .organizationDetails(organizationDetails)
        .build();

    company.setId(UUID.fromString("25074925-f6b6-4c2a-a943-a5bdef2234f2"));

    companyPreferences = CompanyPreferences.builder()
        .company(company)
        .build();

    company.setPreferences(companyPreferences);

    organizationRegistrationForm = new OrganizationRegistrationForm(organizationDetails.getName());

    Set<Role> roles = new HashSet<>();
    roles.add(Role.USER);

    userDetails = AppUserDetails.builder()
        .email("user@dummy.email.test")
        .roles(roles)
        .build();
    userDetails.setId(UUID.randomUUID());

    AppUser appUser = AppUser.builder()
        .id(UUID.randomUUID())
        .userDetails(userDetails)
        .organizationDetails(organizationDetails)
        .build();

    userDetails.setAppUser(appUser);
  }

  @Test
  void RegisterCompany_OnValidFormAndPrincipal_CompanyCreatedAndUserIsGrantedWithNewRoles() {
    when(companyRepository.save(any())).thenReturn(company);
    when(companyPreferencesService.create(any(CompanyPreferences.class))).thenReturn(
        companyPreferences);

    Company createdCompany = companyService.register(organizationRegistrationForm, userDetails);

    assertNotNull(createdCompany);
    assertNotNull(createdCompany.getOrganizationDetails());
    assertEquals(createdCompany.getOrganizationDetails().getName(),
        organizationRegistrationForm.name());
    assertThatCollection(userDetails.getRoles()).contains(Role.USER, Role.EMPLOYEE,
        Role.COMPANY_ADMIN);
  }

  @Test
  void ReadUserCompany_OnCompanyNotExists_CrudValidationExceptionThrown() {
    when(organizationDetailsService.read(userDetails)).thenReturn(organizationDetails);
    when(companyRepository.findByOrganizationDetails(organizationDetails)).thenReturn(
        Optional.empty());

    assertThrows(CrudValidationException.class, () -> companyService.read(userDetails));
  }

  @Test
  void ReadUserCompany_OnCompanyExists_ReturnsCompany() {
    when(organizationDetailsService.read(userDetails)).thenReturn(organizationDetails);
    when(companyRepository.findByOrganizationDetails(organizationDetails)).thenReturn(
        Optional.of(company));

    Company foundCompany = companyService.read(userDetails);

    assertNotNull(foundCompany);
  }

  @Test
  void ReadCompanyById_OnCompanyWithIDNotExists_CrudValidationExceptionThrown() {
    when(companyRepository.findById(any())).thenReturn(Optional.empty());
    UUID randomUUID = UUID.randomUUID();
    assertThrows(CrudValidationException.class, () -> companyService.read(randomUUID));
  }

  @Test
  void ReadCompanyById_OnCompanyWithIDExists_CrudValidationExceptionThrown() {
    when(companyRepository.findById(any())).thenReturn(Optional.of(company));
    Company foundCompany = companyService.read(company.getId());
    assertNotNull(foundCompany);
  }

  @Test
  void UpdateCompany_OnNullCompany_CrudValidationExceptionThrown() {
    when(companyRepository.findById(any())).thenReturn(Optional.of(company));
    Company companyUpdate = Company.builder()
        .preferences(new CompanyPreferences())
        .organizationDetails(new OrganizationDetails())
        .subscriptions(new HashSet<>())
        .subscriptionsRequests(new HashSet<>())
        .build();
    companyUpdate.setId(company.getId());

    Company updated = companyService.update(companyUpdate);

    assertNotNull(updated);
    assertNotNull(updated.getSubscriptions());
    assertEquals(companyUpdate.getSubscriptions(), updated.getSubscriptions());
    assertNotNull(updated.getSubscriptionsRequests());
    assertEquals(companyUpdate.getSubscriptionsRequests(), updated.getSubscriptionsRequests());
  }
}
