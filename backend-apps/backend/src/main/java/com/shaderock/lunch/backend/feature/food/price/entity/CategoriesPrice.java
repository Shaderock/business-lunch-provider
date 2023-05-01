package com.shaderock.lunch.backend.feature.food.price.entity;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
public class CategoriesPrice extends BaseEntity {

  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private double price;

  @Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_preferences_id")
  private SupplierPreferences supplierPreferences;

  @Builder(builderMethodName = "baseEntityBuilder")
  public CategoriesPrice(UUID id) {
    super(id);
  }
}
