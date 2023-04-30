package com.shaderock.lunch.backend.feature.supplier.dto;

import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Supplier} entity
 */
public record SupplierDto(
    UUID id,
    UUID organizationDetailsId,
    URI websiteUrl,
    URI menuUrl,
    @NotNull
    boolean isPublic,
    Set<UUID> subscribersIds,
    UUID preferencesId) implements Serializable {

}
