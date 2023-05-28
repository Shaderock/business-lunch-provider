package com.shaderock.lunch.backend.feature.company.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends DeletableEntityRepository<Company> {

  Optional<Company> findByAppToken(UUID appToken);

  Optional<Company> findByOrganizationDetails(OrganizationDetails organizationDetails);

}
