package com.shaderock.lunch.backend.feature.supplier.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.supplier.entity.Supplier} entity
 */
public record PublicSupplierDto(UUID id,
                                URI websiteUrl,
                                URI menuUrl,
                                UUID organizationDetailsId,
                                UUID preferencesId) implements Serializable {

}
