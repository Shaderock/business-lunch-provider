package com.shaderock.lunch.backend.organization.company.preference.repository;

import com.shaderock.lunch.backend.data.VisibleEntityRepository;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends VisibleEntityRepository<CompanyPreferences> {

  Optional<CompanyPreferences> findByCompany_OrganizationDetails_Users_UserDetails_Id(UUID id);

}
