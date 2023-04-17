package com.shaderock.lunch.backend.feature.config.preference.company.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@SQLDelete(sql = "UPDATE company_preferences SET is_deleted = true WHERE id=?")
public class CompanyPreferences extends VisibleEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  @Exclude
  private Company company;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyDiscountType companyDiscountType;

  @Column
  private String deliveryAddress;
}
