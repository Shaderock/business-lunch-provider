package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.organization.model.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizationRepository<O extends Organization> extends CrudRepository<Organization, Long> {
  Optional<O> findByName(String name);

  Optional<O> findByEmail(String email);

  Optional<O> findByEmailAndDeletedIsFalse(String email);
}
