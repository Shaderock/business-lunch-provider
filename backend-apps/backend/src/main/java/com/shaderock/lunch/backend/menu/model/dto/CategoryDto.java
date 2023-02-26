package com.shaderock.lunch.backend.menu.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.Category} entity
 */
public record CategoryDto(Long id,
                          @NotNull(message = "Name should be provided")
                          @NotBlank(message = "Name can't be blank")
                          String name,
                          Set<Long> optionIds,
                          Long menuId) implements Serializable {

}
