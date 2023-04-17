package com.shaderock.lunch.backend.feature.supplier.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

  @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
  @Exclude
  private Set<Subscription> subscribers;

  @Builder(builderMethodName = "baseEntityBuilder")
  public Supplier(UUID id, boolean isDeleted, boolean isPublic) {
    super(id, isDeleted, isPublic);
  }
}
