package com.shaderock.lunch.backend.feature.company.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@SQLDelete(sql = "UPDATE company SET is_deleted = true WHERE id=?")
public class Company extends DeletableEntity {

  @Exclude
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_details_id", nullable = false)
  private OrganizationDetails organizationDetails;

  @OneToOne(mappedBy = "company")
  private CompanyPreferences preferences;

  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  @Exclude
  private Set<Supplier> subscriptions;

  @ManyToMany(mappedBy = "subscriptionsRequests", fetch = FetchType.LAZY)
  @Exclude
  private Set<Supplier> subscriptionsRequests;

  public Company(UUID id, OrganizationDetails organizationDetails,
      CompanyPreferences preferences, Set<Supplier> subscriptions,
      Set<Supplier> subscriptionsRequests) {
    super(id);
    this.organizationDetails = organizationDetails;
    this.preferences = preferences;
    this.subscriptions = subscriptions;
    this.subscriptionsRequests = subscriptionsRequests;
  }
}
