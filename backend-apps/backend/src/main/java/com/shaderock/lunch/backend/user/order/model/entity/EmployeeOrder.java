package com.shaderock.lunch.backend.user.order.model.entity;

import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.order.model.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.organization.company.model.entity.CompanyOrder;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
