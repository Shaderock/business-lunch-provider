package com.shaderock.lunch.backend.organization.supplier.service;

import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.menu.service.MenuService;
import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.repository.SupplierRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import com.shaderock.lunch.backend.utils.FilterManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
  private final FilterManager filterManager;
  private final SupplierMapper supplierMapper;

  @Transactional
  public Supplier register(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    organizationDetailsService.validateOrganizationRegistration(form, userDetails);

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

  public List<Supplier> readAll() {
    return supplierRepository.findAll();
  }

  public List<Supplier> readAllDeleted() {
    filterManager.enableDeleteFilter();
    List<Supplier> all = readAll();
    filterManager.disableDeleteFilter();
    return all;
  }

  public Supplier read(@NonNull UUID supplierId) {
    return supplierRepository.findById(supplierId).orElseThrow(() -> new CrudValidationException(
        String.format("Supplier(id=[%s] not found", supplierId)));
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
  public void delete(Supplier supplier) {
    LOGGER.info("Attempting to delete {}", supplier);
    if (Objects.isNull(supplier)) {
      throw new CrudValidationException("Can not delete null");
    }

    Supplier persistedSupplier = read(supplier.getId());
    menuService.delete(persistedSupplier.getMenu());
    supplierPreferencesService.delete(persistedSupplier.getPreferences());
    supplierRepository.delete(persistedSupplier);
    organizationDetailsService.delete(persistedSupplier.getOrganizationDetails());

    LOGGER.info("Deleted {}", persistedSupplier);
  }
}
