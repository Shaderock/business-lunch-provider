package com.shaderock.backend.organization.company.preference.model.entity;

import com.shaderock.backend.organization.company.model.entity.Company;
import com.shaderock.backend.organization.company.preference.model.type.CompanyDiscountType;
import com.shaderock.backend.organization.preference.model.PreferenceConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanyPreferenceConfig implements PreferenceConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyDiscountType companyDiscountType;

  @Column(nullable = false)
  private String deliveryAddress;
}
