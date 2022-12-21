package com.shaderock.backend.model.entity.preference.price;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class PriceByCategory extends PriceBy {
  @Serial
  private static final long serialVersionUID = 16L;
  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private double price;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    PriceByCategory that = (PriceByCategory) o;
    return amount == that.amount && Double.compare(that.price, price) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), amount, price);
  }
}
