package com.shaderock.backend.organization.company;

import com.shaderock.backend.organization.company.model.entity.Company;
import com.shaderock.backend.organization.repository.OrganizationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository<C extends Company> extends OrganizationRepository<C> {
}
