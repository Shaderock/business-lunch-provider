package com.shaderock.lunch.backend.organization.supplier.preference.price.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class PriceByCategory extends PriceBy {
  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private double price;
}
