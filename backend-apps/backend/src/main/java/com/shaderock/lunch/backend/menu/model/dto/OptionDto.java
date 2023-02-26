package com.shaderock.lunch.backend.menu.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.menu.model.entity.Option} entity
 */
public record OptionDto(Long id,
                        String name,
                        Long categoryId,
                        Set<Long> employeesOrderIds,
                        List<Long> optionDescriptionIds,
                        Long priceId) implements Serializable {

}
