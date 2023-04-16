package com.shaderock.lunch.backend.menu.model.dto;

import com.shaderock.lunch.backend.menu.model.entity.SubOptions;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link SubOptions} BaseEntity
 */
public record OptionDescriptionDto(UUID id,
                                   String value) implements Serializable {

}
