package com.shaderock.lunch.backend.organization.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.organization.model.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
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
public class OrganizationDetailsService {

  private final OrganizationDetailsRepository organizationDetailsRepository;
  private final OrganizationDetailsValidationService organizationDetailsValidationService;
  private final AppUserDetailsService appUserDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;

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
}
