package com.shaderock.lunch.backend.user.model.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.notification.entity.Notification;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferences;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@SQLDelete(sql = "UPDATE app_user SET is_deleted = true WHERE id=?")
public class AppUser extends DeletableEntity {

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
}
