package com.shaderock.lunch.backend.feature.config.preference.company.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends DeletableEntityRepository<CompanyPreferences> {

  Optional<CompanyPreferences> findByCompany_OrganizationDetails_Users_UserDetails_Id(UUID id);

}
