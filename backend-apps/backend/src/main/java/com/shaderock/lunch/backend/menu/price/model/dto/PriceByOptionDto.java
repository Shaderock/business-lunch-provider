package com.shaderock.lunch.backend.menu.price.model.dto;

import com.shaderock.lunch.backend.menu.price.model.entity.PriceByOption;
import java.io.Serializable;

/**
 * A DTO for the {@link PriceByOption} entity
 */
public record PriceByOptionDto(Long id, double price) implements Serializable {

}
