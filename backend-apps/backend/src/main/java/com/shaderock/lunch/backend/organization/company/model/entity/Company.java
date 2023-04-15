package com.shaderock.lunch.backend.organization.company.model.entity;

import com.shaderock.lunch.backend.data.DeletableEntity;
import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
}
