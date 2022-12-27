package com.shaderock.backend.organization.company.model.entity;

import com.shaderock.backend.order.model.type.CompanyOrderStatus;
import com.shaderock.backend.user.order.model.entity.EmployeeOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanyOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @OneToMany(mappedBy = "companyOrder", fetch = FetchType.LAZY)
  private Set<EmployeeOrder> employeesOrders;

  @Column
  private LocalDateTime deliveryDateTime;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyOrderStatus status;

  @Column
  private boolean isDeleted;
}
