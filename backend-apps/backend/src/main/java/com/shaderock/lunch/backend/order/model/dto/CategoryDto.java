package com.shaderock.lunch.backend.order.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.order.model.entity.Category} entity
 */
public record CategoryDto(
    Long id,
    @NotNull(message = "Name should be provided")
    @NotBlank(message = "Name can't be blank")
    String name
) implements Serializable {

}
