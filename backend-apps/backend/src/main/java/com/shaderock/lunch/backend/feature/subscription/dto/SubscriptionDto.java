package com.shaderock.lunch.backend.feature.subscription.dto;

import com.shaderock.lunch.backend.feature.subscription.type.SubscriptionStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.subscription.entity.Subscription}
 * entity
 */
public record SubscriptionDto(UUID id,
                              UUID companyId,
                              UUID supplierId,
                              SubscriptionStatus subscriptionStatus,
                              LocalDateTime createdAt) implements Serializable {

}
