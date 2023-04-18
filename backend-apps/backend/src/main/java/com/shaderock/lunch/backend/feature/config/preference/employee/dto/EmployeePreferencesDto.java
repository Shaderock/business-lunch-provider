package com.shaderock.lunch.backend.feature.config.preference.employee.dto;

import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link EmployeePreferences} entity
 */
@Builder
public record EmployeePreferencesDto(UUID id,
                                     UUID employeeId) implements Serializable {

}
