package com.shaderock.lunch.backend.feature.config.preference.supplier.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.CategoryTag;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.food.price.entity.PriceForCategories;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
  private Integer minimumOrdersPerCompanyRequest;

  @Column(columnDefinition = "int default 1")
  private Integer minimumCategoriesForEmployeeOrder;

  @Column
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @OneToMany(mappedBy = "supplierPreferences", fetch = FetchType.LAZY)
  @Exclude
  private Set<PriceForCategories> pricesForCategories;

  @OneToOne(mappedBy = "preferences")
  private OrderCapacity orderCapacity;

  @ElementCollection
  @Column(name = "category_tag")
  @CollectionTable(name = "supplier_preferences_category_tag",
      joinColumns = @JoinColumn(name = "owner_id"))
  private List<CategoryTag> categoriesTags = new ArrayList<>();

}
