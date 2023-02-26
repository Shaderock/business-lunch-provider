package com.shaderock.lunch.backend.menu.model.entity;

import com.shaderock.lunch.backend.menu.price.model.entity.PriceByOption;
import com.shaderock.lunch.backend.user.order.model.entity.EmployeeOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
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
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column
  private boolean isDeleted;

  @ManyToMany(mappedBy = "options", fetch = FetchType.LAZY)
  private Set<EmployeeOrder> employeesOrders;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  private List<OptionDescription> optionDescriptions;

  @OneToOne(mappedBy = "option")
  private PriceByOption price;
}
