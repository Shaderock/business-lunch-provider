package com.shaderock.backend.model.entity.user;

import com.shaderock.backend.model.entity.company.Company;
import com.shaderock.backend.model.entity.preference.EmployeePreferenceConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Employee extends AppUser {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Company company;

  @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
  private EmployeePreferenceConfig preferences;

  @ManyToOne(fetch = FetchType.LAZY)
  private Company companyRequest;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Employee employee = (Employee) o;
    return company.equals(employee.company) && Objects.equals(preferences, employee.preferences) && Objects.equals(companyRequest, employee.companyRequest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), company, preferences, companyRequest);
  }
}
