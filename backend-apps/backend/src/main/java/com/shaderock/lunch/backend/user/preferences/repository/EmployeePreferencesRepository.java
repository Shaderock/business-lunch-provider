package com.shaderock.lunch.backend.user.preferences.repository;

import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferences;
import org.springframework.data.repository.ListCrudRepository;

public interface EmployeePreferencesRepository extends
    ListCrudRepository<EmployeePreferences, Long> {

}
