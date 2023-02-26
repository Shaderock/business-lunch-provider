package com.shaderock.lunch.backend.user.model.entity;

import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferences;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
}
