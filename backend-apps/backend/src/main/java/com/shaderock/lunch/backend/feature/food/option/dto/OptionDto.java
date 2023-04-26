package com.shaderock.lunch.backend.feature.food.option.dto;

import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Option}
 */
public record OptionDto(
    UUID id,
    String name,
    UUID categoryId,
    Double price,
    String gram,
    String description,
    boolean isPublic,
    boolean hasPhoto,
    LocalDate createdAt,
    LocalDate publishedAt,
    Set<UUID> employeesOrderIds,
    List<UUID> subOptionsIds) implements Serializable {

}
