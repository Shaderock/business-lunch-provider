package com.shaderock.backend.repository.preference;

import com.shaderock.backend.model.entity.preference.CompanyPreferenceConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends CrudRepository<CompanyPreferenceConfig, Long> {
}
