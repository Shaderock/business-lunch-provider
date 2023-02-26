package com.shaderock.lunch.backend.user.preferences.model.entity;

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
//@Builder
@Getter
@Setter
@Entity
public class EmployeeNotifications extends NotificationConfig {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeePreferences preferenceConfig;
}
