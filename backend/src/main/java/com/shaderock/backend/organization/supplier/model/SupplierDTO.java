package com.shaderock.backend.organization.supplier.model;

import lombok.Builder;

@Builder
public record SupplierDTO(
        String name,
        String email,
        String description,
        String phone,
        String websiteUrl,
        String menuUrl,
        SupplierPreferencesDTO preferences) {
}
