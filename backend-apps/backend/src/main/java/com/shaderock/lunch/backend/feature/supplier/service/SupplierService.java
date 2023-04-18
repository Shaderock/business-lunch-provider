package com.shaderock.lunch.backend.feature.supplier.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.service.SupplierPreferencesService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.menu.service.MenuService;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsValidationService;
import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.feature.supplier.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {

  private final SupplierRepository supplierRepository;
  private final OrganizationDetailsService organizationDetailsService;
  private final MenuService menuService;
  private final SupplierPreferencesService supplierPreferencesService;
  private final SupplierMapper supplierMapper;
  private final OrganizationDetailsValidationService organizationDetailsValidationService;

  @Transactional(TxType.REQUIRES_NEW)
  public Supplier register(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    organizationDetailsValidationService.validateOrganizationRegistration(form, userDetails);

    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name(form.name())
        .build();

    Supplier persistedSupplier = create(organizationDetails);
    userDetails.getAppUser().setOrganizationDetails(persistedSupplier.getOrganizationDetails());
    userDetails.getRoles().add(Role.SUPPLIER);

    return persistedSupplier;
  }

  @Transactional
  public Supplier create(@NonNull Supplier supplier, @NonNull OrganizationDetails details) {
    Supplier persistedSupplier = create(details);
    supplier.setId(persistedSupplier.getId());

    update(supplier);
    return persistedSupplier;
  }

  @Transactional
  public Supplier create(@NonNull OrganizationDetails details) {
    OrganizationDetails organizationDetails = organizationDetailsService.create(details);

    Supplier supplier = Supplier.builder()
        .organizationDetails(organizationDetails)
        .build();
    Supplier persistedSupplier = supplierRepository.save(supplier);

    Menu menu = Menu.builder().supplier(persistedSupplier).build();
    Menu persistedMenu = menuService.create(menu);
    persistedSupplier.setMenu(persistedMenu);

    SupplierPreferences preferences = SupplierPreferences.builder().supplier(persistedSupplier)
        .build();
    SupplierPreferences persistedPreferences = supplierPreferencesService.create(preferences);
    persistedSupplier.setPreferences(persistedPreferences);

    return persistedSupplier;
  }

  public List<Supplier> read() {
    return supplierRepository.findAll();
  }

  public Supplier read(@NonNull UUID supplierId) {
    return supplierRepository.findById(supplierId).orElseThrow(() -> new CrudValidationException(
        String.format("Supplier(id=[%s] not found", supplierId)));
  }

  public Supplier read(@NonNull Principal principal) {
    return read(principal.getName());
  }

  public Supplier read(@NonNull String userEmail) {
    return supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(userEmail)
        .orElseThrow(() -> new CrudValidationException("Supplier organization not found for user"));
  }

  public Supplier update(@NonNull SupplierDto supplierDto) {
    Supplier supplier = supplierMapper.toEntity(supplierDto);
    return update(supplier);
  }

  @Transactional
  public Supplier update(@NonNull Supplier supplier) {
    LOGGER.info("Attempting to update {}", supplier);

    Supplier persistedSupplier = read(supplier.getId());
    persistedSupplier.setWebsiteUrl(supplier.getWebsiteUrl());
    persistedSupplier.setMenuUrl(supplier.getMenuUrl());

    LOGGER.info("Updated {}", persistedSupplier);
    return persistedSupplier;
  }

  @Transactional
  public void delete(@NonNull AppUserDetails userDetails) {
    LOGGER.info("Attempting to delete {}", userDetails);

    Supplier persistedSupplier = read(userDetails.getId());
    menuService.delete(persistedSupplier.getMenu());
    supplierPreferencesService.delete(userDetails);
    supplierRepository.delete(persistedSupplier);
    organizationDetailsService.delete(persistedSupplier.getOrganizationDetails());

    LOGGER.info("Deleted {}", persistedSupplier);
  }
}
