package com.shaderock.lunch.backend.organization.supplier.preference.model.entity;

import com.shaderock.lunch.backend.menu.price.model.entity.PriceBy;
import com.shaderock.lunch.backend.organization.preference.model.PreferenceConfig;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SupplierPreferenceConfig implements PreferenceConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "supplier_id", nullable = false)
  private Supplier supplier;
  @Column(nullable = false)
  private Duration requestOffset;

  @Column(nullable = false)
  private LocalTime deliveryPeriodStartTime;

  @Column(nullable = false)
  private LocalTime deliveryPeriodEndTime;

  @Column(columnDefinition = "int default 1")
  private int minimumOrdersPerRequest;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @OneToMany(mappedBy = "preferenceConfig", fetch = FetchType.LAZY)
  private Set<PriceBy> prices; // todo should be one and not set

  @OneToOne(mappedBy = "preferenceConfig")
  private OrderCapacity orderCapacity;
}
