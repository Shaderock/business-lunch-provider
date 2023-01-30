package com.shaderock.lunch.backend.organization.supplier.preference.price.model.dto;

import java.io.Serializable;

/**
 * A DTO for the
 * {@link
 * com.shaderock.lunch.backend.organization.supplier.preference.price.model.entity.PriceByOption}
 * entity
 */
public record PriceByOptionDto(Long id, double price) implements Serializable {

}
