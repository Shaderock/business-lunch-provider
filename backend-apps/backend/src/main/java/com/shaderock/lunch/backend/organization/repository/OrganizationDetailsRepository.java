package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface OrganizationDetailsRepository extends
    ListCrudRepository<OrganizationDetails, UUID> {

  Optional<OrganizationDetails> findByName(String name);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
