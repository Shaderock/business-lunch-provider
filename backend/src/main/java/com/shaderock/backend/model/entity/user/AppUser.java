package com.shaderock.backend.model.entity.user;

import com.shaderock.backend.model.entity.organization.Organization;
import com.shaderock.backend.model.entity.preference.EmployeePreferenceConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "appUser", fetch = FetchType.LAZY, optional = false)
  private AppUserDetails userDetails;

  @ManyToOne(fetch = FetchType.LAZY)
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY)
  private Organization organizationRequest;

  @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
  private EmployeePreferenceConfig preferences;
}
