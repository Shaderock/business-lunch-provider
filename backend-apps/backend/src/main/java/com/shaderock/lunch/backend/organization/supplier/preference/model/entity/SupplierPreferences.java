package com.shaderock.lunch.backend.organization.supplier.preference.model.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.DELETED_FILTER;

import com.shaderock.lunch.backend.menu.price.model.entity.PriceForCategories;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.PriceByType;
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
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "supplier_preferences")
@SQLDelete(sql = "UPDATE supplier_preferences SET deleted = true WHERE id=?")
@FilterDef(name = DELETED_FILTER, parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = DELETED_FILTER, condition = "deleted = :isDeleted")
public class SupplierPreferences  {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private boolean deleted = false;
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
  private int minimumOrdersPerRequest;

  @Column
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column
  @Enumerated(EnumType.STRING)
  private PriceByType priceByType;

  @OneToMany(mappedBy = "supplierPreferences", fetch = FetchType.LAZY)
  @Exclude
  private Set<PriceForCategories> priceForCategories;

  @OneToOne(mappedBy = "preferenceConfig")
  private OrderCapacity orderCapacity;
}
