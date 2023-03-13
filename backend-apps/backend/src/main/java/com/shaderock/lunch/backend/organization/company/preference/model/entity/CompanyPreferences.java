package com.shaderock.lunch.backend.organization.company.preference.model.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.DELETED_FILTER;

import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.preference.model.type.CompanyDiscountType;
import com.shaderock.lunch.backend.organization.preference.model.PreferenceConfig;
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
import jakarta.persistence.Table;
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
@Table(name = "companies_preferences")
@SQLDelete(sql = "UPDATE companies_preferences SET deleted = true WHERE id=?")
@FilterDef(name = DELETED_FILTER, parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = DELETED_FILTER, condition = "deleted = :isDeleted")
public class CompanyPreferences implements PreferenceConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private boolean deleted = false;

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
