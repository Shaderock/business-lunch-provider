package com.shaderock.lunch.backend.organization.service;

import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.security.Principal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationService {

  private final AppUserDetailsService userDetailsService;
  private final OrganizationRepository organizationRepository;

  public Optional<Organization> getUserOrganization(Principal principal) {
    LOGGER.info("Searching organization for Principal(name=[{}])", principal.getName());
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Organization organization = userDetails.getAppUser().getOrganization();

    LOGGER.info("Found [{}]", organization);
    return Optional.ofNullable(organization);
  }

  public boolean existsByName(String name) {
    return organizationRepository.existsByName(name);
  }

  public boolean existsByEmail(String email) {
    return organizationRepository.existsByEmail(email);
  }
}
