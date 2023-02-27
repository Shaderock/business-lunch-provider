package com.shaderock.lunch.backend.organization.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import com.shaderock.lunch.backend.utils.FilterManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationDetailsService {

  private final AppUserDetailsService userDetailsService;
  private final OrganizationDetailsRepository organizationDetailsRepository;
  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final FilterManager filterManager;

  public void validateOrganizationRegistration(OrganizationRegistrationForm form,
      Principal principal) {
    LOGGER.info("Validating organization registration by [{}]", form);

    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

    if (userDetails.getRoles().size() > 1 || !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(
          String.format("User(email=[%s]) can not register organizations", userDetails.getEmail()));
    }

    LOGGER.info("Organization is valid for registration");
  }

  @Transactional
  public OrganizationDetails create(OrganizationDetails details) {
    LOGGER.info("Attempting to create {}", details);

    validateCreate(details);

    OrganizationDetails persistedDetails = organizationDetailsRepository.save(
        details);

    LOGGER.info("Created {}", persistedDetails);
    return persistedDetails;
  }

  public void validateCreate(OrganizationDetails details) {
    if (Objects.isNull(details)) {
      throw new ValidationException("Can not create null");
    }

    if (StringUtils.isBlank(details.getName())) {
      throw new ValidationException("Can not create organization without name");
    }

    if (existsByName(details.getName())) {
      throw new ValidationException(String.format("Organization(name=[%s]) already exists",
          details.getName()));
    }
  }

  public OrganizationDetails readByPrincipal(Principal principal) {
    LOGGER.info("Searching organization details by Principal(name=[{}])", principal.getName());
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    OrganizationDetails organizationDetails = userDetails.getAppUser().getOrganizationDetails();
    if (Objects.isNull(organizationDetails)) {
      throw new CrudValidationException(
          String.format("Principal(name=[%s]) does not have an organization", principal.getName()));
    }

    LOGGER.info("Found [{}]", organizationDetails);
    return organizationDetails;
  }

  public List<OrganizationDetails> readAll() {
    return organizationDetailsRepository.findAll();
  }

  public List<OrganizationDetails> readAllDeleted() {
    filterManager.enableDeleteFilter();
    List<OrganizationDetails> allDetails = readAll();
    filterManager.disableDeleteFilter();
    return allDetails;
  }

  public OrganizationDetails read(UUID id) {
    LOGGER.info("Searching organization details by id=[{}])", id);
    OrganizationDetails persistedDetails = organizationDetailsRepository.findById(id)
        .orElseThrow(() ->
            new CrudValidationException(String.format("Organization(id=[%s] not found", id)));
    LOGGER.info("Found [{}]", persistedDetails);
    return persistedDetails;
  }

  @Transactional
  public OrganizationDetails update(OrganizationDetails details) {
    LOGGER.info("Attempting to update {}", details);

    validateUpdate(details);

    OrganizationDetails persistedDetails = organizationDetailsRepository.findById(
        details.getId()).orElseThrow(() -> new CrudValidationException(
        String.format("Organization(id=[%s]) not found", details.getId())));

    persistedDetails.setName(details.getName());
    persistedDetails.setDescription(details.getDescription());
    persistedDetails.setEmail(details.getEmail());
    persistedDetails.setPhone(details.getPhone());

    LOGGER.info("Updated {}", persistedDetails);
    return persistedDetails;
  }

  public void validateUpdate(OrganizationDetails details) {
    if (Objects.isNull(details)) {
      throw new ValidationException("Can not update null");
    }

    if (StringUtils.isBlank(details.getName())) {
      throw new ValidationException("Can not update organization without name");
    }
  }

  @Transactional
  public void delete(UUID id) {
    if (Objects.isNull(id)) {
      throw new CrudValidationException("Can not delete null");
    }

    organizationDetailsRepository.deleteById(id);
  }

  @Transactional
  public void delete(OrganizationDetails details) {
    if (Objects.isNull(details)) {
      throw new CrudValidationException("Can not delete null");
    }
    delete(details.getId());
  }

  public boolean existsByName(String name) {
    return organizationDetailsRepository.existsByName(name);
  }

  public boolean existsByEmail(String email) {
    return organizationDetailsRepository.existsByEmail(email);
  }
}
