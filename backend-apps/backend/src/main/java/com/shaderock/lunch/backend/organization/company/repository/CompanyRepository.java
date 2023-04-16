package com.shaderock.lunch.backend.organization.company.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import java.util.Optional;

public interface CompanyRepository extends DeletableEntityRepository<Company> {

  Optional<Company> findByOrganizationDetails(OrganizationDetails organizationDetails);

}
