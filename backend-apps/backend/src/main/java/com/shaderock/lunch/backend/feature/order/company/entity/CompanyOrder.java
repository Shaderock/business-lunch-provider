package com.shaderock.lunch.backend.feature.order.company.entity;

import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.model.type.CompanyOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanyOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(mappedBy = "companyOrder", fetch = FetchType.LAZY)
  @Exclude
  private Set<EmployeeOrder> employeesOrders;

  @Column
  private LocalDateTime deliveryDateTime;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyOrderStatus status;

  @Column
  private boolean isDeleted;
}
