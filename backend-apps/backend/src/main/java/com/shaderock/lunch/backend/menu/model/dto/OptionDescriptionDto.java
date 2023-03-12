package com.shaderock.lunch.backend.menu.model.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.OptionDescription} entity
 */
public record OptionDescriptionDto(UUID id,
                                   String value) implements Serializable {

}
