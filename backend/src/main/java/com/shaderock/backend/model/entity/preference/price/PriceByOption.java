package com.shaderock.backend.model.entity.preference.price;

import com.shaderock.backend.model.entity.menu.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class PriceByOption extends PriceBy {
  @Serial
  private static final long serialVersionUID = 17L;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "option_id", nullable = false)
  private Option option;

  @Column(nullable = false)
  private double price;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    PriceByOption that = (PriceByOption) o;
    return Double.compare(that.price, price) == 0 && option.equals(that.option);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), option, price);
  }
}
