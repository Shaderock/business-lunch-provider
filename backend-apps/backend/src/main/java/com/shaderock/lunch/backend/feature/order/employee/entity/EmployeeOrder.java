package com.shaderock.lunch.backend.feature.order.employee.entity;

import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.model.type.EmployeeOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmployeeOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column
  private boolean isDeleted;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_order_id")
  private CompanyOrder companyOrder;

  @JoinTable(name = "employees_orders_options",
      joinColumns = @JoinColumn(name = "employee_order_id"),
      inverseJoinColumns = @JoinColumn(name = "option_id"))
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Option> options;

  @Column
  private double supplierDefaultPrice;
  @Column
  private double supplierDiscountedPrice;
  @Column
  private double finalPrice;

  @Column
  @Enumerated(EnumType.STRING)
  private EmployeeOrderStatus status;
}
