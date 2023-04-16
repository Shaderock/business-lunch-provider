package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.Optional;

public interface OrganizationDetailsRepository extends
    DeletableEntityRepository<OrganizationDetails> {

  Optional<OrganizationDetails> findByName(String name);

  Optional<OrganizationDetails> findByUsers_UserDetails(AppUserDetails userDetails);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
