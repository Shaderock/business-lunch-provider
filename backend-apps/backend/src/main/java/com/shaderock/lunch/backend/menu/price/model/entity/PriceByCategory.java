package com.shaderock.lunch.backend.menu.price.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class PriceByCategory extends PriceBy {

  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private double price;
}
