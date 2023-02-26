package com.shaderock.lunch.backend.user.model.dto;

import java.io.Serializable;
import lombok.Builder;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.user.model.entity.AppUser} entity
 */
@Builder
public record AppUserDto(Long id,
                         Long detailsId,
                         Long organizationId,
                         Long organizationRequestId,
                         Long preferencesId) implements Serializable {

}
