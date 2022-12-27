package com.shaderock.backend.model.entity.menu;

import com.shaderock.backend.model.entity.order.EmployeeOrder;
import com.shaderock.backend.model.entity.preference.price.PriceByOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private Category category;

  @Column
  private boolean isDeleted;

  @ManyToMany(mappedBy = "options", fetch = FetchType.LAZY)
  private Set<EmployeeOrder> employeesOrders;

  @Column
  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  private List<OptionDescription> optionDescriptions;

  @OneToOne(mappedBy = "option", fetch = FetchType.LAZY)
  private PriceByOption price;
}
