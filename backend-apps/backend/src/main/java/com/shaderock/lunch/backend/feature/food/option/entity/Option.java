package com.shaderock.lunch.backend.feature.food.option.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.suboption.entity.SubOption;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
@Builder(builderMethodName = "optionBuilder")
@Entity
@ToString
@SQLDelete(sql = "UPDATE option SET is_deleted = true WHERE id=?")
public class Option extends VisibleEntity {

  @Column
  private String name;
  @Column
  private Double price;
  @Column
  private String description;

  @Exclude
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @Exclude
  private List<SubOption> subOptions;

  @Exclude
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "option_employee_orders",
      joinColumns = @JoinColumn(name = "option_id"),
      inverseJoinColumns = @JoinColumn(name = "employee_orders_id"))
  private Collection<EmployeeOrder> employeesOrders = new ArrayList<>();

  @Builder
  public Option(UUID id, boolean isDeleted, boolean isPublic) {
    super(id, isDeleted, isPublic);
  }
}
