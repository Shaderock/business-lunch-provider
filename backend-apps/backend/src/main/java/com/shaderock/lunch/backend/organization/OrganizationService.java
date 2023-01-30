package com.shaderock.lunch.backend.organization;

import com.shaderock.lunch.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationService {
  private final AppUserDetailsService userDetailsService;
  private final OrganizationRepository<Organization> organizationRepository;

  @Transactional
  public Organization getUserOrganization(Principal principal) {
    log.info("Searching organization for [{}]", principal);
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Organization organization = userDetails.getAppUser().getOrganization();

    if (Objects.isNull(organization)) {
      throw new TransferableApplicationException("Assigned organization not found");
    }

    log.info("Found [{}]", organization);
    return organization;
  }

  public boolean doesOrganizationExistByName(String name) {
    return organizationRepository.findByName(name).isPresent();
  }

  public boolean doesOrganizationExistByEmail(String email) {
    return organizationRepository.findByEmail(email).isPresent();
  }
}
