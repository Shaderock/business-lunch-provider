package com.shaderock.lunch.backend.user.preferences.model.dto;

import java.io.Serializable;
import lombok.Builder;

/**
 * A DTO for the
 * {@link com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig}
 * entity
 */
@Builder
public record EmployeePreferencesDto(Long id,
                                     Long notificationConfigId,
                                     Long employeeId) implements Serializable {

}
