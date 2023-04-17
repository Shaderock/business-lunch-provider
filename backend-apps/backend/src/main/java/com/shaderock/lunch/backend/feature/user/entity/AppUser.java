package com.shaderock.lunch.backend.feature.user.entity;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class AppUser extends BaseEntity {

  @OneToOne(mappedBy = "appUser", optional = false)
  private AppUserDetails userDetails;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id")
  @Exclude
  private OrganizationDetails organizationDetails;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_request_id")
  @Exclude
  private OrganizationDetails organizationDetailsRequest;

  @OneToOne(mappedBy = "employee")
  private EmployeePreferences preferences;

  @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
  @Exclude
  private List<Notification> notifications = new ArrayList<>();

  @Builder(builderMethodName = "baseEntityBuilder")
  public AppUser(UUID id) {
    super(id);
  }
}
