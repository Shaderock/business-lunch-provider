package com.shaderock.lunch.backend.feature.user.dto;

import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link AppUser} entity
 */
@Builder
public record AppUserDto(UUID id,
                         UUID detailsId,
                         UUID organizationId,
                         UUID notificationConfigId,
                         List<UUID> invitationsIds,
                         UUID preferencesId) implements Serializable {

}
