package com.shaderock.lunch.backend.menu.price.model.entity;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PriceForCategories {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private int amount;
  @Column(nullable = false)
  private double price;
  @Exclude
  @ManyToOne
  @JoinColumn(name = "supplier_preferences_id")
  private SupplierPreferences supplierPreferences;
}
