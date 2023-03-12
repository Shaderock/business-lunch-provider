package com.shaderock.lunch.backend.organization.company.preference.repository;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends ListCrudRepository<CompanyPreferences, UUID> {

  Optional<CompanyPreferences> findByCompany_OrganizationDetails_Users_UserDetails_Id(UUID id);

}
