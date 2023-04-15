package com.shaderock.lunch.backend.organization.supplier.model.entity;

import com.shaderock.lunch.backend.data.DeletableEntity;
import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
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
@SQLDelete(sql = "UPDATE supplier SET is_deleted = true WHERE id=?")
public class Supplier extends DeletableEntity {

  @Column
  private URI websiteUrl;

  @Column
  private URI menuUrl;

  @Column(nullable = false)
  private boolean isPublic = false;
  @Exclude
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_details_id", nullable = false)
  private OrganizationDetails organizationDetails;

  @Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  private Menu menu;

  @Exclude
  @OneToOne(mappedBy = "supplier")
  private SupplierPreferences preferences;

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
}
