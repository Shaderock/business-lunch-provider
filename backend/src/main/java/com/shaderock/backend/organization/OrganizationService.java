package com.shaderock.backend.organization;

import com.shaderock.backend.messaging.exception.TransferableApplicationException;
import com.shaderock.backend.organization.model.Organization;
import com.shaderock.backend.organization.repository.OrganizationRepository;
import com.shaderock.backend.user.AppUserDetailsService;
import com.shaderock.backend.user.model.entity.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrganizationService {
  private final AppUserDetailsService userDetailsService;
  private final OrganizationRepository<Organization> organizationRepository;

  @Transactional
  public Organization getUserOrganization(Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Organization organization = userDetails.getAppUser().getOrganization();

    if (Objects.isNull(organization)) {
      throw new TransferableApplicationException("Assigned organization not found");
    }

    return organization;
  }

  public boolean doesOrganizationExistByName(String name) {
    return organizationRepository.findByName(name).isPresent();
  }

  public boolean doesOrganizationExistByEmail(String email) {
    return organizationRepository.findByEmail(email).isPresent();
  }
}
