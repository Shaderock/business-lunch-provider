package com.shaderock.backend.model.entity.preference.notification;

import com.shaderock.backend.model.entity.preference.CompanyPreferenceConfig;
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
public class CompanyNotificationConfig extends NotificationConfig {
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "preference_config_id", nullable = false)
  private CompanyPreferenceConfig preferenceConfig;
}
