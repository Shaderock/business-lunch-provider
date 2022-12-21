package com.shaderock.backend.model.entity.order;

import com.shaderock.backend.model.entity.menu.Option;
import com.shaderock.backend.model.type.EmployeeOrderStatus;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmployeeOrder implements Serializable {
  @Serial
  private static final long serialVersionUID = 5L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private boolean isDeleted;

  @ManyToOne(fetch = FetchType.LAZY)
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
