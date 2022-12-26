package com.shaderock.backend.repository.preference;

import com.shaderock.backend.model.entity.preference.CompanyPreferenceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPreferenceRepository extends JpaRepository<CompanyPreferenceConfig, Long> {
}
