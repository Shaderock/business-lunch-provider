package com.shaderock.lunch.backend.user.preferences.model.dto;

import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferences;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link EmployeePreferences} BaseEntity
 */
@Builder
public record EmployeePreferencesDto(UUID id,
                                     UUID notificationConfigId,
                                     UUID employeeId) implements Serializable {

}
