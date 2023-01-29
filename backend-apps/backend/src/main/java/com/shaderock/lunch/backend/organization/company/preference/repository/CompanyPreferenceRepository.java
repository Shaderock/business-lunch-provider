package com.shaderock.lunch.backend.organization.company.preference.repository;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferenceConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends CrudRepository<CompanyPreferenceConfig, Long> {
}
