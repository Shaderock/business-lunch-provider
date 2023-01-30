package com.shaderock.lunch.backend.order.model.dto;

import com.shaderock.lunch.backend.organization.supplier.preference.price.model.dto.PriceByOptionDto;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.order.model.entity.Option} entity
 */
public record OptionDto(Long id, List<OptionDescriptionDto> optionDescriptions,
                        PriceByOptionDto price) implements Serializable {

}
