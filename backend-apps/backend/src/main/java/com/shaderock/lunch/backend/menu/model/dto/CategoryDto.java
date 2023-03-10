package com.shaderock.lunch.backend.menu.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.Category} entity
 */
public record CategoryDto(UUID id,
                          @NotNull(message = "Name should be provided")
                          @NotBlank(message = "Name can't be blank")
                          String name,
                          Set<UUID> optionIds,
                          boolean isOrderingAllowed,
                          UUID menuId) implements Serializable {

}
