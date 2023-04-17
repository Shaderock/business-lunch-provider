package com.shaderock.lunch.backend.feature.supplier.dto;

import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.io.Serializable;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Supplier} entity
 */
public record SupplierDto(UUID id,
                          UUID organizationDetailsId,
                          URI websiteUrl,
                          URI menuUrl,
                          boolean isPublic,
                          Set<UUID> subscribersIds,
                          Set<UUID> subscriptionsRequestsIds,
                          UUID preferencesId) implements Serializable {

}
