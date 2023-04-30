package com.shaderock.lunch.backend.feature.order.company.entity;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import com.shaderock.lunch.backend.feature.order.company.type.CompanyOrderStatus;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class CompanyOrder extends BaseEntity {

  @OneToMany(mappedBy = "companyOrder", fetch = FetchType.LAZY)
  @Exclude
  private Set<EmployeeOrder> employeesOrders;

  @Column
  private LocalDateTime deliverAt;

  @Column
  private LocalDateTime createdAt;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyOrderStatus status;
}
