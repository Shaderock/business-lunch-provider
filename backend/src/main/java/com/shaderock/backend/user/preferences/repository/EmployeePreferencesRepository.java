package com.shaderock.backend.user.preferences.repository;

import com.shaderock.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import org.springframework.data.repository.CrudRepository;

public interface EmployeePreferencesRepository extends CrudRepository<EmployeePreferenceConfig, Long> {
}
