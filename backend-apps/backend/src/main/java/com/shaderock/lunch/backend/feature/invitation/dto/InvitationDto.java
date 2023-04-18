package com.shaderock.lunch.backend.feature.invitation.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.invitation.entity.Invitation} entity
 */
public record InvitationDto(UUID id,
                            LocalDateTime createdAt,
                            UUID companyId,
                            UUID appUserId) implements Serializable {

}
