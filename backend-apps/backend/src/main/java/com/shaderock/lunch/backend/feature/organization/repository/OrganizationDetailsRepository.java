package com.shaderock.lunch.backend.feature.organization.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import java.util.Optional;

public interface OrganizationDetailsRepository extends
    DeletableEntityRepository<OrganizationDetails> {

  Optional<OrganizationDetails> findByName(String name);

  Optional<OrganizationDetails> findByUsers_UserDetails(AppUserDetails userDetails);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
