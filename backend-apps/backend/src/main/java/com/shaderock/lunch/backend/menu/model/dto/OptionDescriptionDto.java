package com.shaderock.lunch.backend.menu.model.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.OptionDescription} entity
 */
public record OptionDescriptionDto(Long id,
                                   String value) implements Serializable {

}
