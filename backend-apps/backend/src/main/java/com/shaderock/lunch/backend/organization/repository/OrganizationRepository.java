package com.shaderock.lunch.backend.organization.repository;

import com.shaderock.lunch.backend.organization.model.Organization;
import java.util.Optional;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Where(clause = "is_deleted = false")
public interface OrganizationRepository<O extends Organization> extends
    CrudRepository<Organization, Long> {

  Optional<O> findByName(String name);

  Optional<O> findByEmail(String email);

  @Query("select o from Organization o inner join o.users users where users.userDetails.email = ?1")
  Optional<O> findByUsers_UserDetails_Email(String email);
}
