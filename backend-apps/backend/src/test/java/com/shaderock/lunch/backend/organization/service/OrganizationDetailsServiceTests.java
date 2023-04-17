package com.shaderock.lunch.backend.organization.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsValidationService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.user.type.Role;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.List;
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
class OrganizationDetailsServiceTests {

  @Mock
  private OrganizationDetailsRepository organizationDetailsRepository;
  @InjectMocks
  private OrganizationDetailsService organizationDetailsService;
  @InjectMocks
  private OrganizationDetailsValidationService organizationDetailsValidationService;
  @Mock
  private AppUserDetailsService appUserDetailsService;
  private OrganizationDetails organizationDetails;
  private AppUserDetails userDetails;
  private AppUser appUser;
  private Principal principal;

  @BeforeEach
  public void init() {
    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name("organization")
        .phone("+373777777")
        .email("organization@dummy.email.test")
        .build();
    organizationDetails.setId(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"));

    userDetails = AppUserDetails.builder()
        .email("user@dummy.email.test")
        .build();
    userDetails.setId(UUID.fromString("0b566090-69b2-48ef-ba4f-6788248bfdb7"));

    appUser = AppUser.builder()
        .userDetails(userDetails)
        .organizationDetails(organizationDetails)
        .build();
    appUser.setId(UUID.fromString("d18489b8-b959-4189-94c3-371a4d1498a2"));

    userDetails.setAppUser(appUser);

    principal = new UserPrincipal(userDetails.getEmail());
  }

  @Test
  void ValidateOrganizationRegistration_OnInvalidUserRole_ValidationExceptionThrown() {
    OrganizationRegistrationForm registrationForm = new OrganizationRegistrationForm(
        "organization");

    userDetails.setRoles(Set.of(Role.COMPANY_ADMIN));

    assertThrows(CompanyRegistrationValidationException.class,
        () -> organizationDetailsValidationService.validateOrganizationRegistration(
            registrationForm,
            userDetails));
  }

  @Test
  void ValidateOrganizationRegistration_OnValidUserAndRegistrationForm_NoExceptionThrown() {
    OrganizationRegistrationForm registrationForm = new OrganizationRegistrationForm(
        "organization");

    userDetails.setRoles(Set.of(Role.USER));

    assertDoesNotThrow(
        () -> organizationDetailsValidationService.validateOrganizationRegistration(
            registrationForm,
            userDetails));
  }

  @Test
  void CreateOrganization_OnBlankName_ValidationExceptionThrown() {
    organizationDetails.setName("");

    assertThrows(ValidationException.class,
        () -> organizationDetailsService.create(organizationDetails));
  }

  @Test
  void CreateOrganization_OnNullName_ValidationExceptionThrown() {
    organizationDetails.setName(null);

    assertThrows(ValidationException.class,
        () -> organizationDetailsService.create(organizationDetails));
  }

  @Test
  void CreateOrganization_OnOrganizationExists_ValidationExceptionThrown() {
    when(organizationDetailsRepository.existsByName(anyString())).thenReturn(
        true);

    assertThrows(ValidationException.class,
        () -> organizationDetailsService.create(organizationDetails));
  }

  @Test
  void CreateOrganization_OnValidOrganization_ReturnsOrganizationCreated() {
    when(organizationDetailsRepository.existsByName(anyString())).thenReturn(false);
    when(organizationDetailsRepository.save(any(OrganizationDetails.class))).thenReturn(
        organizationDetails);

    OrganizationDetails created = organizationDetailsService.create(
        organizationDetails);

    assertNotNull(created);
  }

  @Test
  void ReadOrganizationByPrincipal_OnUserHasNoOrganization_CrudValidationExceptionThrown() {
    appUser.setOrganizationDetails(null);

    assertThrows(CrudValidationException.class,
        () -> organizationDetailsService.read(userDetails));
  }

  @Test
  void ReadOrganizationByPrincipal_OnUserHasOrganization_ReturnsOrganization() {
    OrganizationDetails createdDetails = organizationDetailsService.read(userDetails);

    assertNotNull(createdDetails);
  }

  @Test
  void ReadAllOrganizations_OnOrganizationsExist_ReturnsListOfOrganizations() {
    when(organizationDetailsRepository.findAll()).thenReturn(List.of(organizationDetails));

    List<OrganizationDetails> allDetails = organizationDetailsService.read();

    assertNotNull(allDetails);
  }

  @Test
  void ReadOrganization_OnOrganizationDoesNotExist_CrudValidationExceptionThrown() {
    when(organizationDetailsRepository.findById(any())).thenReturn(
        Optional.empty());

    UUID id = organizationDetails.getId();

    assertThrows(CrudValidationException.class, () -> organizationDetailsService.read(id));
  }

  @Test
  void ReadOrganization_OnOrganizationExists_ReturnsOrganization() {
    when(organizationDetailsRepository.findById(organizationDetails.getId())).thenReturn(
        Optional.of(organizationDetails));

    OrganizationDetails readDetails = organizationDetailsService.read(organizationDetails.getId());
    assertNotNull(readDetails);
  }

  @Test
  void UpdateOrganization_OnOrganizationWithoutNamePassed_ValidationExceptionThrown() {
    organizationDetails.setName(null);
    assertThrows(CrudValidationException.class,
        () -> organizationDetailsService.update(organizationDetails));
  }

  @Test
  void UpdateOrganization_OnOrganizationNotFound_ValidationExceptionThrown() {
    when(organizationDetailsRepository.findById(organizationDetails.getId())).thenReturn(
        Optional.empty());
    assertThrows(CrudValidationException.class,
        () -> organizationDetailsService.update(organizationDetails));
  }

  @Test
  void UpdateOrganization_OnValidOrganizationPassed_ReturnsUpdatedOrganization() {
    OrganizationDetails detailsForUpdate = OrganizationDetails.builder()
        .email("newemail@dummy.mail.test")
        .name("new name")
        .phone("+37388888888")
        .description("new description")
        .build();
    detailsForUpdate.setId(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"));

    when(organizationDetailsRepository.findById(organizationDetails.getId())).thenReturn(
        Optional.of(organizationDetails));

    OrganizationDetails updated = organizationDetailsService.update(detailsForUpdate);

    assertNotNull(updated);
    assertEquals("newemail@dummy.mail.test", updated.getEmail());
    assertEquals("new name", updated.getName());
    assertEquals("+37388888888", updated.getPhone());
    assertEquals("new description", updated.getDescription());
  }

  @Test
  void DeleteOrganization_OnNullOrganizationPassed_ReturnsUpdatedOrganization() {
    UUID nullId = UUID.randomUUID();
    assertThrows(CrudValidationException.class, () -> organizationDetailsService.delete(nullId));
  }

  @Test
  void DeleteOrganization_OnOrganizationNotFound_ReturnsUpdatedOrganization() {
    when(organizationDetailsRepository.findById(organizationDetails.getId())).thenReturn(
        Optional.empty());

    assertThrows(CrudValidationException.class,
        () -> organizationDetailsService.delete(organizationDetails));
  }
}
