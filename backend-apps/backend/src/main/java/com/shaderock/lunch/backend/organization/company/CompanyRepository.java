package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.repository.OrganizationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository<C extends Company> extends OrganizationRepository<C> {
}
