package com.shaderock.lunch.backend.notification.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link Notification} entity
 */
public record NotificationDto(UUID id,
                              LocalDateTime createdAt,
                              boolean isViewed,
                              boolean isSentByBrowser,
                              String content) implements Serializable {

}
