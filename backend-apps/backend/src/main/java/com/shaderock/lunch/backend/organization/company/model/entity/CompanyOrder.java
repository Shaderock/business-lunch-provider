package com.shaderock.lunch.backend.organization.company.model.entity;

import com.shaderock.lunch.backend.order.model.type.CompanyOrderStatus;
import com.shaderock.lunch.backend.user.order.model.entity.EmployeeOrder;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
