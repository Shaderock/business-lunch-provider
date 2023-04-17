package com.shaderock.lunch.backend.feature.supplier.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.net.URI;
import java.util.Set;
import java.util.UUID;
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
public class Supplier extends VisibleEntity {

  @Column
  private URI websiteUrl;

  @Column
  private URI menuUrl;

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

  public Supplier(UUID id, boolean isPublic, URI websiteUrl, URI menuUrl,
      OrganizationDetails organizationDetails, Menu menu, SupplierPreferences preferences,
      Set<Company> subscribers, Set<Company> subscriptionsRequests) {
    super(id, isPublic);
    this.websiteUrl = websiteUrl;
    this.menuUrl = menuUrl;
    this.organizationDetails = organizationDetails;
    this.menu = menu;
    this.preferences = preferences;
    this.subscribers = subscribers;
    this.subscriptionsRequests = subscriptionsRequests;
  }
}
