package com.shaderock.lunch.backend.organization.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.preference.model.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.model.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.organization.company.preference.model.type.CompanyDiscountType;
import com.shaderock.lunch.backend.organization.company.preference.repository.CompanyPreferenceRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyPreferencesServiceTests {

  @Mock
  private CompanyPreferenceRepository companyPreferenceRepository;
  @Mock
  private CompanyPreferencesMapper companyPreferencesMapper;
  @InjectMocks
  private CompanyPreferencesServiceImpl companyPreferencesService;
  private CompanyPreferences preferences;
  private CompanyPreferencesDto preferencesDto;
  private AppUserDetails userDetails;

  @BeforeEach
  public void init() {
    userDetails = AppUserDetails.builder()
        .id(UUID.randomUUID())
        .email("user@dummy.email.test")
        .build();

    preferences = CompanyPreferences.builder()
        .id(UUID.randomUUID())
        .companyDiscountType(CompanyDiscountType.PERCENTAGE_FIRST)
        .deliveryAddress("Delivery address")
        .build();

    preferencesDto = new CompanyPreferencesDto(preferences.getId(), UUID.randomUUID(),
        preferences.getCompanyDiscountType(), preferences.getDeliveryAddress());
  }

  @Test
  void CreatePreferences_OnValidDto_ReturnsCreatedPreferences() {
    when(companyPreferenceRepository.save(any())).thenReturn(preferences);
    when(companyPreferencesMapper.toEntity(any())).thenReturn(preferences);
    CompanyPreferences created = companyPreferencesService.create(preferencesDto);
    assertNotNull(created);
  }

  @Test
  void ReadPreferencesById_OnCompanyNotExists_CrudValidationExceptionThrown() {
    when(companyPreferenceRepository.findById(any())).thenReturn(Optional.empty());
    UUID id = preferences.getId();
    assertThrows(CrudValidationException.class, () -> companyPreferencesService.read(id));
  }

  @Test
  void ReadPreferencesById_OnCompanyExists_ReturnsPreferences() {
    when(companyPreferenceRepository.findById(any())).thenReturn(Optional.of(preferences));
    CompanyPreferences read = companyPreferencesService.read(preferences.getId());
    assertNotNull(read);
  }

  @Test
  void ReadPreferencesByUserDetails_OnCompanyExists_ReturnsPreferences() {
    when(companyPreferenceRepository.findByCompany_OrganizationDetails_Users_UserDetails_Id(
        any())).thenReturn(Optional.of(preferences));
    CompanyPreferences read = companyPreferencesService.read(userDetails);
    assertNotNull(read);
  }

  @Test
  void ReadPreferencesByUserDetails_OnCompanyNotExists_CrudValidationThrown() {
    when(companyPreferenceRepository.findByCompany_OrganizationDetails_Users_UserDetails_Id(
        any())).thenReturn(Optional.empty());
    assertThrows(CrudValidationException.class, () -> companyPreferencesService.read(userDetails));
  }

  @Test
  void UpdatePreferencesByDto_OnValidDto_ReturnsUpdatedPreferences() {
    when(companyPreferenceRepository.findByCompany_OrganizationDetails_Users_UserDetails_Id(
        any())).thenReturn(Optional.of(preferences));
    when(companyPreferencesMapper.toEntity(any())).thenReturn(preferences);

    CompanyPreferences updated = companyPreferencesService.update(preferencesDto, userDetails);
    assertNotNull(updated);
    assertEquals(preferencesDto.deliveryAddress(), updated.getDeliveryAddress());
    assertEquals(preferencesDto.companyDiscountType(), updated.getCompanyDiscountType());
  }
}
