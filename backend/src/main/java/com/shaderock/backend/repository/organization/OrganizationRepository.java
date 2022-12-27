package com.shaderock.backend.repository.organization;

import com.shaderock.backend.model.entity.organization.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
  Optional<Organization> findByName(String name);

  Optional<Organization> findByEmail(String email);

  Optional<Organization> findByEmailAndDeletedIsFalse(String email);
}
