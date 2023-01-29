package com.shaderock.lunch.backend.order.model.menu;

import com.shaderock.lunch.backend.organization.supplier.preference.price.model.entity.PriceByOption;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Option {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column
  private boolean isDeleted;

  @ManyToMany(mappedBy = "options", fetch = FetchType.LAZY)
  private Set<EmployeeOrder> employeesOrders;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  private List<OptionDescription> optionDescriptions;

  @OneToOne(mappedBy = "option", fetch = FetchType.LAZY)
  private PriceByOption price;
}
