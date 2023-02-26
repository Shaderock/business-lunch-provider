package com.shaderock.lunch.backend.organization.company.repository;

import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface CompanyRepository extends ListCrudRepository<Company, UUID> {

  Optional<Company> findByOrganizationDetails(OrganizationDetails organizationDetails);

}
