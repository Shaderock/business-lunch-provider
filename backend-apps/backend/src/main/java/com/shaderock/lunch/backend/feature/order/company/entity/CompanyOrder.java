package com.shaderock.lunch.backend.feature.order.company.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
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
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@SQLDelete(sql = "UPDATE company_order SET is_deleted = true WHERE id=?")
public class CompanyOrder extends DeletableEntity {

  @OneToMany(mappedBy = "companyOrder", fetch = FetchType.LAZY)
  @Exclude
  private Set<EmployeeOrder> employeesOrders;

  @Column
  private LocalDateTime deliveryDateTime;

  @Column
  @Enumerated(EnumType.STRING)
  private CompanyOrderStatus status;
}
