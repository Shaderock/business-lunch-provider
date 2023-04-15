package com.shaderock.lunch.backend.organization.supplier.service;

import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.shaderock.lunch.backend.menu.service.MenuService;
import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.preference.service.SupplierPreferencesService;
import com.shaderock.lunch.backend.organization.supplier.repository.SupplierRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import java.net.URI;
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
class SupplierServiceTests {

  @Mock
  private SupplierRepository supplierRepository;
  @Mock
  private OrganizationDetailsService organizationDetailsService;
  @Mock
  private MenuService menuService;
  @Mock
  private SupplierPreferencesService supplierPreferencesService;
  @InjectMocks
  private SupplierService supplierService;
  private OrganizationDetails organizationDetails;
  private Supplier supplier;
  private AppUserDetails userDetails;
  private OrganizationRegistrationForm organizationRegistrationForm;
  private SupplierPreferences supplierPreferences;

  @BeforeEach
  public void init() {
    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name("organization")
        .phone("+373777777")
        .email("organization@dummy.email.test")
        .build();
    organizationDetails.setId(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"));

    supplier = Supplier.builder()
        .organizationDetails(organizationDetails)
        .build();
    supplier.setId(UUID.fromString("25074925-f6b6-4c2a-a943-a5bdef2234f2"));

    supplierPreferences = SupplierPreferences.builder()
        .supplier(supplier)
        .build();

    supplier.setPreferences(supplierPreferences);

    organizationRegistrationForm = new OrganizationRegistrationForm(organizationDetails.getName());

    Set<Role> roles = new HashSet<>();
    roles.add(Role.USER);

    userDetails = AppUserDetails.builder()
        .email("user@dummy.email.test")
        .roles(roles)
        .build();
    userDetails.setId(UUID.randomUUID());

    AppUser appUser = AppUser.builder()
        .userDetails(userDetails)
        .organizationDetails(organizationDetails)
        .build();
    appUser.setId(UUID.randomUUID());

    userDetails.setAppUser(appUser);
  }

  @Test
  void RegisterSupplier_OnValidFormAndPrincipal_SupplierCreatedAndUserIsGrantedWithSupplierRole() {
    when(supplierRepository.save(any())).thenReturn(supplier);
    when(supplierPreferencesService.create(any(SupplierPreferences.class))).thenReturn(
        supplierPreferences);

    Supplier createdSupplier = supplierService.register(organizationRegistrationForm, userDetails);

    assertNotNull(createdSupplier);
    assertNotNull(createdSupplier.getOrganizationDetails());
    assertEquals(createdSupplier.getOrganizationDetails().getName(),
        organizationRegistrationForm.name());
    assertThatCollection(userDetails.getRoles()).contains(Role.USER, Role.SUPPLIER);
  }

  @Test
  void ReadSupplierById_OnSupplierExists_ReturnsSupplier() {
    when(supplierRepository.findById((any()))).thenReturn(Optional.of(supplier));
    Supplier readSupplier = supplierService.read(supplier.getId());
    assertNotNull(readSupplier);
  }

  @Test
  void ReadSupplierById_OnSupplierNotExists_CrudValidationExceptionThrown() {
    when(supplierRepository.findById((any()))).thenReturn(Optional.empty());
    UUID id = supplier.getId();
    assertThrows(CrudValidationException.class, () -> supplierService.read(id));
  }

  @Test
  void ReadSupplierByUserEmail_OnSupplierExists_ReturnsSupplier() {
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email((any()))).thenReturn(
        Optional.of(supplier));
    Supplier readSupplier = supplierService.read(userDetails.getEmail());
    assertNotNull(readSupplier);
  }

  @Test
  void ReadSupplierByUserEmail_OnSupplierNotExists_CrudValidationExceptionThrown() {
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email((any())))
        .thenReturn(Optional.empty());
    String email = userDetails.getEmail();
    assertThrows(CrudValidationException.class, () -> supplierService.read(email));
  }

  @Test
  void UpdateSupplier_OnValidSupplier_ReturnsNewSupplier() {
    when(supplierRepository.findById((any()))).thenReturn(Optional.of(supplier));

    supplier.setWebsiteUrl(URI.create("http://first"));
    supplier.setMenuUrl(URI.create("http://second"));
    Supplier supplierUpdate = Supplier.builder()
        .organizationDetails(supplier.getOrganizationDetails())
        .websiteUrl(URI.create("http://third"))
        .menuUrl(URI.create("http://fourth"))
        .build();
    supplierUpdate.setId(supplier.getId());
    Supplier updatedSupplier = supplierService.update(supplierUpdate);

    assertNotNull(updatedSupplier);
    assertEquals(supplierUpdate.getWebsiteUrl(), updatedSupplier.getWebsiteUrl());
    assertEquals(supplierUpdate.getMenuUrl(), updatedSupplier.getMenuUrl());
  }

}
