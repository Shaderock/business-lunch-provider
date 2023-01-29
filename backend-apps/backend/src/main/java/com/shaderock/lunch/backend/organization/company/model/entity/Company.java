package com.shaderock.lunch.backend.organization.company.model.entity;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferenceConfig;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company extends Organization {
  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  private Set<Supplier> subscriptions;

  @ManyToMany(mappedBy = "subscriptionsRequests", fetch = FetchType.LAZY)
  private Set<Supplier> subscriptionsRequest;

  @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
  private CompanyPreferenceConfig preferences;
}
