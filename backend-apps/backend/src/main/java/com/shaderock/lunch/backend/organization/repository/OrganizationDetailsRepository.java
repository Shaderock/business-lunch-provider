package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.data.DeletableEntityRepository;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import java.util.Optional;

public interface OrganizationDetailsRepository extends
    DeletableEntityRepository<OrganizationDetails> {

  Optional<OrganizationDetails> findByName(String name);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
