package com.shaderock.lunch.backend.feature.config.preference.company.repository;

import com.shaderock.lunch.backend.data.repository.VisibleEntityRepository;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends VisibleEntityRepository<CompanyPreferences> {

  Optional<CompanyPreferences> findByCompany_OrganizationDetails_Users_UserDetails_Id(UUID id);

}
