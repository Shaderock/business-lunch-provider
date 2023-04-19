package com.shaderock.lunch.backend.feature.config.preference.company.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
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
public class CompanyPreferences extends DeletableEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  @Exclude
  private Company company;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyDiscountType companyDiscountType;

  @Column
  private Integer discountPercentageFirstOrder;

  @Column
  private Double discountFixFirstOrder;

  @Column
  private Double maxDiscountFixFirstOrder;

  @Column
  private Double discountPerDay;

  @Column
  private String deliveryAddress;

  @Column
  private LocalTime deliverAt;

  @Builder(builderMethodName = "baseEntityBuilder")
  public CompanyPreferences(UUID id, boolean isDeleted) {
    super(id, isDeleted);
  }
}
