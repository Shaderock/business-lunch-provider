package com.shaderock.lunch.backend.organization.supplier.model.entity;

import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.model.Organization;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferenceConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.net.URI;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString(callSuper = true)
public class Supplier extends Organization {

  @Column(nullable = false)
  private URI websiteUrl;
  @Column(nullable = false)
  private URI menuUrl;

  @JoinTable(name = "lunch_subscriptions",
      joinColumns = @JoinColumn(name = "supplier_id"),
      inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  @Exclude
  private Set<Company> subscribers;

  @JoinTable(name = "lunch_subscriptions_requests",
      joinColumns = @JoinColumn(name = "supplier_id"),
      inverseJoinColumns = @JoinColumn(name = "company_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  @Exclude
  private Set<Company> subscriptionsRequests;

  @OneToOne(mappedBy = "supplier")
  private Menu menu;

  @OneToOne(mappedBy = "supplier")
  private SupplierPreferenceConfig preferences;
}
