package com.shaderock.lunch.backend.feature.config.notification.employee.entity;

import com.shaderock.lunch.backend.data.entity.NotificationConfig;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class EmployeeNotificationConfig extends NotificationConfig {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_user_id")
  @Exclude
  private AppUser appUser;

}
