package com.shaderock.backend.model.entity.user;

import com.shaderock.backend.model.entity.menu.Menu;
import com.shaderock.backend.model.entity.preference.SupplierPreferenceConfig;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Supplier extends AppUser {
  @Column(nullable = false)
  private String name;
  private String description;
  @Column(nullable = false)
  private String phone;
  private byte[] logo;
  @Column(nullable = false)
  private URI websiteUrl;
  @Column(nullable = false)
  private URI menuUrl;

  @Column
  private boolean isDeleted;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Supplier supplier = (Supplier) o;
    return isDeleted == supplier.isDeleted && name.equals(supplier.name) && description.equals(supplier.description) &&
            phone.equals(supplier.phone) && Arrays.equals(logo, supplier.logo)
            && websiteUrl.equals(supplier.websiteUrl) && menuUrl.equals(supplier.menuUrl) &&
            Objects.equals(subscribers, supplier.subscribers) &&
            Objects.equals(subscriptionsRequests, supplier.subscriptionsRequests) &&
            Objects.equals(menu, supplier.menu) && Objects.equals(preferences, supplier.preferences);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), name, description, phone, websiteUrl, menuUrl, isDeleted,
                              subscribers, subscriptionsRequests, menu, preferences);
    result = 31 * result + Arrays.hashCode(logo);
    return result;
  }
}
