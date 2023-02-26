package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.organization.model.Organization;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

  Optional<Organization> findByName(String name);

  Optional<Organization> findByEmail(String email);

  @Query("select o from Organization o inner join o.users users where users.userDetails.email = ?1")
  Optional<Organization> findByUsers_UserDetails_Email(String email);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
