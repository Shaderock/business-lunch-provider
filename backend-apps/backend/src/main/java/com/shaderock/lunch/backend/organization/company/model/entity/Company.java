package com.shaderock.lunch.backend.organization.company.model.entity;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferenceConfig;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class Company extends Organization {

  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  @Exclude
  private Set<Supplier> subscriptions;

  @ManyToMany(mappedBy = "subscriptionsRequests", fetch = FetchType.LAZY)
  @Exclude
  private Set<Supplier> subscriptionsRequest;

  @OneToOne(mappedBy = "company")
  private CompanyPreferenceConfig preferences;
}
