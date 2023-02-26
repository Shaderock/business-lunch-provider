package com.shaderock.lunch.backend.organization.company.preference.repository;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends ListCrudRepository<CompanyPreferences, Long> {

}
