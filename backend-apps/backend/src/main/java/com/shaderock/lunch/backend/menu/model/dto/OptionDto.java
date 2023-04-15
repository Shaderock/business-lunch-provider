package com.shaderock.lunch.backend.menu.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.Option} BaseEntity
 */
public record OptionDto(UUID id,
                        String name,
                        UUID categoryId,
                        Double price,
                        boolean isPublic,
                        Set<UUID> employeesOrderIds,
                        List<UUID> optionDescriptionIds,
                        UUID priceId) implements Serializable {

}
