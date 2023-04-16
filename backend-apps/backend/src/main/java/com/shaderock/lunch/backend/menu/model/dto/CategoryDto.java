package com.shaderock.lunch.backend.menu.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.Category}
 */
public record CategoryDto(UUID id,
                          @NotNull(message = "Name should be provided")
                          @NotBlank(message = "Name can't be blank")
                          String name,
                          Set<UUID> optionIds,
                          @NotNull(message = "Visibility should be provided")
                          Boolean isPublic) implements Serializable {

}
