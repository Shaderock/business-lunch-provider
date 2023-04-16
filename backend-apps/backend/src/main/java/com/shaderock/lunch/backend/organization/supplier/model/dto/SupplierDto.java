package com.shaderock.lunch.backend.organization.supplier.model.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier}
 * entity
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
