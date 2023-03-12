package com.shaderock.lunch.backend.user.model.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.user.model.entity.AppUser} entity
 */
@Builder
public record AppUserDto(UUID id,
                         UUID detailsId,
                         UUID organizationId,
                         UUID organizationRequestId,
                         UUID preferencesId) implements Serializable {

}
