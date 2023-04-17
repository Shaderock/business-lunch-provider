package com.shaderock.lunch.backend.feature.food.option.dto;

import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Option}
 */
public record OptionDto(UUID id,
                        String name,
                        UUID categoryId,
                        Double price,
                        boolean isPublic,
                        Set<UUID> employeesOrderIds,
                        List<UUID> subOptionsIds) implements Serializable {

}
