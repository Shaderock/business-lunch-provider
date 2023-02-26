package com.shaderock.lunch.backend.user.preferences.model.dto;

import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferences;
import java.io.Serializable;
import lombok.Builder;

/**
 * A DTO for the {@link EmployeePreferences} entity
 */
@Builder
public record EmployeePreferencesDto(Long id,
                                     Long notificationConfigId,
                                     Long employeeId) implements Serializable {

}
