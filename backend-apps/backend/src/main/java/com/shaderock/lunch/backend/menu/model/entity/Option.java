package com.shaderock.lunch.backend.menu.model.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.user.order.model.entity.EmployeeOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Set;
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

  @ManyToMany(mappedBy = "options", fetch = FetchType.LAZY)
  @Exclude
  private Set<EmployeeOrder> employeesOrders;

  @Exclude
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @Exclude
  private List<SubOptions> subOptions;

  @Builder
  public Option(UUID id, boolean isPublic, String name, Double price, String description,
      Set<EmployeeOrder> employeesOrders, Category category, List<SubOptions> subOptions) {
    super(id, isPublic);
    this.name = name;
    this.price = price;
    this.description = description;
    this.employeesOrders = employeesOrders;
    this.category = category;
    this.subOptions = subOptions;
  }
}
