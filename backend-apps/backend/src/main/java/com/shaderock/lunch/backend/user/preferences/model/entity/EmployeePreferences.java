package com.shaderock.lunch.backend.user.preferences.model.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE employee_preferences SET is_deleted = true WHERE id=?")
public class EmployeePreferences extends DeletableEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "notification_config_id", nullable = false)
  private EmployeeNotifications notificationConfig;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private AppUser employee;
}
