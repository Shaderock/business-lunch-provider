package com.shaderock.lunch.backend.feature.organization.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.feature.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.feature.supplier.repository.SupplierRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationDetailsService {

  private final OrganizationDetailsRepository organizationDetailsRepository;
  private final OrganizationDetailsValidationService organizationDetailsValidationService;
  private final AppUserDetailsService appUserDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final CacheManager cacheManager;
  private final SupplierRepository supplierRepository;

  @Transactional
  public OrganizationDetails create(@NonNull OrganizationDetails details) {
    LOGGER.info("Attempting to create {}", details);

    organizationDetailsValidationService.validateCreate(details);
    OrganizationDetails persistedDetails = organizationDetailsRepository.save(details);

    LOGGER.info("Created {}", persistedDetails);
    return persistedDetails;
  }

  public List<OrganizationDetails> read() {
    return organizationDetailsRepository.findAll();
  }

  public OrganizationDetails read(@NonNull String userEmail) {
    AppUserDetails userDetails = appUserDetailsService.loadUserByUsername(userEmail);
    return read(userDetails);
  }

  public OrganizationDetails read(@NonNull AppUserDetails userDetails) {
    LOGGER.info("Searching organization details by UserDetails(email=[{}])",
        userDetails.getEmail());

    OrganizationDetails organizationDetails = organizationDetailsRepository.findByUsers_UserDetails(
        userDetails).orElseThrow(() -> new CrudValidationException(
        String.format("UserDetails(email=[%s]) does not have an organization",
            userDetails.getEmail())));

    LOGGER.info("Found [{}]", organizationDetails);
    return organizationDetails;
  }

  public OrganizationDetails read(@NonNull UUID id) {
    LOGGER.info("Searching organization details by id=[{}])", id);

    OrganizationDetails persistedDetails = organizationDetailsRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(
            String.format("OrganizationDetails(id=[%s] not found", id)));

    LOGGER.info("Found [{}]", persistedDetails);
    return persistedDetails;
  }

  @SneakyThrows
  @Transactional
  public void updateLogo(MultipartFile logo, OrganizationDetails organizationDetails) {
    organizationDetails.setLogo(logo.getBytes());

    supplierRepository.findByOrganizationDetails(organizationDetails).ifPresent(supplier -> {
      Cache cache = cacheManager.getCache("logo");
      if (cache != null) {
        cacheManager.getCacheNames().stream()
            .filter(cacheKey -> cacheKey.startsWith(supplier.getId().toString()))
            .forEach(cache::evict);
      }
    });
  }

  public OrganizationDetails update(@NonNull OrganizationDetailsDto organizationDetailsDto,
      @NonNull AppUserDetails appUserDetails) {
    OrganizationDetails persisted = read(appUserDetails);
    OrganizationDetails toUpdate = organizationDetailsMapper.toEntity(organizationDetailsDto);
    toUpdate.setId(persisted.getId());
    return update(toUpdate);
  }

  public OrganizationDetails update(@NonNull OrganizationDetailsDto detailsDto) {
    return update(organizationDetailsMapper.toEntity(detailsDto));
  }

  @Transactional
  public OrganizationDetails update(@NonNull OrganizationDetails details) {
    LOGGER.info("Attempting to update {}", details);
    OrganizationDetails persistedDetails = read(details.getId());
    organizationDetailsValidationService.validateUpdate(details);

    persistedDetails.setName(details.getName());
    persistedDetails.setDescription(details.getDescription());
    persistedDetails.setEmail(details.getEmail());
    persistedDetails.setPhone(details.getPhone());

    organizationDetailsRepository.save(persistedDetails);

    LOGGER.info("Updated {}", persistedDetails);
    return persistedDetails;
  }

  @Transactional
  public void delete(@NonNull UUID id) {
    OrganizationDetails organizationDetails = read(id);
    delete(organizationDetails);
  }

  @Transactional
  public void delete(@NonNull OrganizationDetails details) {
    organizationDetailsRepository.delete(details);
  }

  public boolean existsByName(@NonNull String name) {
    return organizationDetailsRepository.existsByName(name);
  }

  public boolean existsByEmail(@NonNull String email) {
    return organizationDetailsRepository.existsByEmail(email);
  }

  @Transactional
  public void addUser(@NonNull AppUser appUser,
      @NonNull OrganizationDetails organizationDetails) {
    organizationDetails.getUsers().add(appUser);
    appUser.setOrganizationDetails(organizationDetails);
  }

  @Transactional
  public void removeUser(@NonNull AppUser appUser,
      @NonNull OrganizationDetails organizationDetails) {
    organizationDetailsValidationService.validateRemoveUser(appUser, organizationDetails);
    organizationDetails.getUsers().remove(appUser);
    appUser.setOrganizationDetails(null);
  }
}
