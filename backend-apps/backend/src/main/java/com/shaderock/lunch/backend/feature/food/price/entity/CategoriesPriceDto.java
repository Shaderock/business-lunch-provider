package com.shaderock.lunch.backend.feature.food.price.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link CategoriesPrice} entity
 */
public record CategoriesPriceDto(
    UUID id,
    @NotNull
    @Min(1)
    Integer amount,
    @NotNull
    @DecimalMin(value = "0.01")
    Double price,
    UUID supplierPreferencesId) implements Serializable {

}
