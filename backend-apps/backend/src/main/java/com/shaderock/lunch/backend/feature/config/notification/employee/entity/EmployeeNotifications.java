package com.shaderock.lunch.backend.feature.config.notification.employee.entity;

import com.shaderock.lunch.backend.data.entity.NotificationConfig;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
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
public class EmployeeNotifications extends NotificationConfig {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private EmployeePreferences preferenceConfig;
}
