package com.shaderock.lunch.backend.organization.supplier.preference.model.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.menu.price.model.entity.PriceForCategories;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.SQLDelete;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@SQLDelete(sql = "UPDATE supplier_preferences SET is_deleted = true WHERE id=?")
public class SupplierPreferences extends DeletableEntity {

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id", nullable = false)
  @Exclude
  private Supplier supplier;
  @Column
  private Duration requestOffset;

  @Column
  private LocalTime deliveryPeriodStartTime;

  @Column
  private LocalTime deliveryPeriodEndTime;

  @Column(columnDefinition = "int default 1")
  private int minimumOrdersPerCompanyRequest;

  @Column
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @OneToMany(mappedBy = "supplierPreferences", fetch = FetchType.LAZY)
  @Exclude
  private Set<PriceForCategories> priceForCategories;

  @OneToOne(mappedBy = "preferences")
  private OrderCapacity orderCapacity;
}
