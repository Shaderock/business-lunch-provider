package com.shaderock.lunch.backend.feature.food.option.dto;

import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO for the {@link Option}
 */
public record PublicOptionDto(
    UUID id,
    String name,
    UUID categoryId,
    Double price,
    String gram,
    String description,
    boolean hasPhoto,
    LocalDate createdAt,
    LocalDate publishedAt) implements Serializable {

}
