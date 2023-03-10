package com.shaderock.lunch.backend.organization.company.preference.model.entity;

import com.shaderock.lunch.backend.organization.preference.notification.model.entity.NotificationConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CompanyNotifications extends NotificationConfig {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "preference_config_id", nullable = false)
  private CompanyPreferences preferences;
}
