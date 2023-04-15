package com.shaderock.lunch.backend.organization.service;

import com.shaderock.lunch.backend.messaging.exception.CrudValidationException;
import com.shaderock.lunch.backend.organization.company.model.error.exception.CompanyRegistrationValidationException;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.repository.OrganizationDetailsRepository;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationDetailsService {

  private final OrganizationDetailsRepository organizationDetailsRepository;

  public void validateOrganizationRegistration(@NonNull OrganizationRegistrationForm form,
      @NonNull AppUserDetails userDetails) {
    LOGGER.info("Validating organization registration by [{}]", form);

    if (Objects.isNull(userDetails.getRoles())) {
      throw new CompanyRegistrationValidationException(
          "Can not register an organization for a user without roles");
    }

    if (userDetails.getRoles().size() > 1 || !userDetails.getRoles().contains(Role.USER)) {
      throw new CompanyRegistrationValidationException(
          String.format("User(email=[%s]) can not register organizations", userDetails.getEmail()));
    }

    LOGGER.info("Organization is valid for registration");
  }

  @Transactional
  public OrganizationDetails create(@NonNull OrganizationDetails details) {
    LOGGER.info("Attempting to create {}", details);

    validateCreate(details);

    OrganizationDetails persistedDetails = organizationDetailsRepository.save(details);

    LOGGER.info("Created {}", persistedDetails);
    return persistedDetails;
  }

  public void validateCreate(@NonNull OrganizationDetails details) {
    if (StringUtils.isBlank(details.getName())) {
      throw new CrudValidationException("Can not create organization without name");
    }

    if (findByName(details.getName()).isPresent()) {
      throw new CrudValidationException(String.format("Organization(name=[%s]) already exists",
          details.getName()));
    }
  }

  public OrganizationDetails read(@NonNull AppUserDetails userDetails) {
    LOGGER.info("Searching organization details by UserDetails(email=[{}])",
        userDetails.getEmail());
    OrganizationDetails organizationDetails = userDetails.getAppUser().getOrganizationDetails();
    if (Objects.isNull(organizationDetails)) {
      throw new CrudValidationException(
          String.format("UserDetails(email=[%s]) does not have an organization",
              userDetails.getEmail()));
    }

    LOGGER.info("Found [{}]", organizationDetails);
    return organizationDetails;
  }

  public List<OrganizationDetails> readAll() {
    return organizationDetailsRepository.findAll();
  }

  public OrganizationDetails read(@NonNull UUID id) {
    LOGGER.info("Searching organization details by id=[{}])", id);
    OrganizationDetails persistedDetails = organizationDetailsRepository.findById(id)
        .orElseThrow(() ->
            new CrudValidationException(String.format("Organization(id=[%s] not found", id)));
    LOGGER.info("Found [{}]", persistedDetails);
    return persistedDetails;
  }

  @Transactional
  public OrganizationDetails update(@NonNull OrganizationDetails details) {
    LOGGER.info("Attempting to update {}", details);

    OrganizationDetails persistedDetails = validateUpdate(details);

    persistedDetails.setName(details.getName());
    persistedDetails.setDescription(details.getDescription());
    persistedDetails.setEmail(details.getEmail());
    persistedDetails.setPhone(details.getPhone());

    LOGGER.info("Updated {}", persistedDetails);
    return persistedDetails;
  }

  public OrganizationDetails validateUpdate(@NonNull OrganizationDetails details) {
    if (StringUtils.isBlank(details.getName())) {
      throw new CrudValidationException("Can not update organization without name");
    }

    return organizationDetailsRepository.findById(
        details.getId()).orElseThrow(() -> new CrudValidationException(
        String.format("Organization(id=[%s]) not found", details.getId())));
  }

  @Transactional
  public void delete(@NonNull UUID id) {
    if (organizationDetailsRepository.findById(id).isEmpty()) {
      throw new CrudValidationException(String.format("Organization(id=[%s]) not found", id));
    }

    organizationDetailsRepository.deleteById(id);
  }

  @Transactional
  public void delete(@NonNull OrganizationDetails details) {
    delete(details.getId());
  }

  public boolean existsByName(@NonNull String name) {
    return organizationDetailsRepository.existsByName(name);
  }

  public Optional<OrganizationDetails> findByName(String name) {
    return organizationDetailsRepository.findByName(name);
  }

  public boolean existsByEmail(@NonNull String email) {
    return organizationDetailsRepository.existsByEmail(email);
  }
}
