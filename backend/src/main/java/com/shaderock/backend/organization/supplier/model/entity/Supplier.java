package com.shaderock.backend.organization.supplier.model.entity;

import com.shaderock.backend.order.model.menu.Menu;
import com.shaderock.backend.organization.company.model.entity.Company;
import com.shaderock.backend.organization.model.Organization;
import com.shaderock.backend.organization.supplier.preference.model.entity.SupplierPreferenceConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Supplier extends Organization {
  @Column(nullable = false)
  private URI websiteUrl;
  @Column(nullable = false)
  private URI menuUrl;

  @JoinTable(name = "lunch_subscriptions",
          joinColumns = @JoinColumn(name = "supplier_id"),
          inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Company> subscribers;

  @JoinTable(name = "lunch_subscriptions_requests",
          joinColumns = @JoinColumn(name = "supplier_id"),
          inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Company> subscriptionsRequests;

  @OneToOne(mappedBy = "supplier", fetch = FetchType.LAZY)
  private Menu menu;

  @OneToOne(mappedBy = "supplier", fetch = FetchType.LAZY)
  private SupplierPreferenceConfig preferences;
}
