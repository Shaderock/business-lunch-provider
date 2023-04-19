package com.shaderock.lunch.backend.feature.order.employee.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@SQLDelete(sql = "UPDATE employee_order SET is_deleted = true WHERE id=?")
public class EmployeeOrder extends DeletableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_order_id")
  @Exclude
  private CompanyOrder companyOrder;

  @Column
  private double supplierDefaultPrice;
  @Column
  private double supplierDiscount;
  @Column
  private double companyDiscount;
  @Column
  private double finalPrice;
  @Column
  private LocalDate orderDate;

  @Column
  @Enumerated(EnumType.STRING)
  private EmployeeOrderStatus status;

  @ManyToMany(mappedBy = "employeesOrders", fetch = FetchType.LAZY)
  @Exclude
  private Collection<Option> options = new ArrayList<>();

  @Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_user_id")
  private AppUser appUser;

  @Builder(builderMethodName = "baseEntityBuilder")
  public EmployeeOrder(UUID id, boolean isDeleted) {
    super(id, isDeleted);
  }
}
