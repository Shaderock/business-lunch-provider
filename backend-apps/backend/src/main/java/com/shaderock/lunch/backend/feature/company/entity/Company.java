package com.shaderock.lunch.backend.feature.company.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @Exclude
  private Set<Subscription> subscriptions;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @Exclude
  private List<Invitation> invitations = new ArrayList<>();

  @Column
  private UUID appToken;

  @Builder
  public Company(UUID id, boolean isDeleted, OrganizationDetails organizationDetails,
      CompanyPreferences preferences, Set<Subscription> subscriptions) {
    super(id, isDeleted);
    this.organizationDetails = organizationDetails;
    this.preferences = preferences;
    this.subscriptions = subscriptions;
  }

  @PrePersist
  public void assignAppToken() {
    if (appToken == null) {
      appToken = UUID.randomUUID();
    }
  }
}
