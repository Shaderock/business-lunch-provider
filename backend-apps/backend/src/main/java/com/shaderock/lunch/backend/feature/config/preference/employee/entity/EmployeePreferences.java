package com.shaderock.lunch.backend.feature.config.preference.employee.entity;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmployeePreferences extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private AppUser employee;

  @Builder
  public EmployeePreferences(UUID id, AppUser employee) {
    super(id);
    this.employee = employee;
  }
}
