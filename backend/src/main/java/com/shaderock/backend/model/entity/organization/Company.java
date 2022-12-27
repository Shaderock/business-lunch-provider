package com.shaderock.backend.model.entity.organization;

import com.shaderock.backend.model.entity.preference.CompanyPreferenceConfig;
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
