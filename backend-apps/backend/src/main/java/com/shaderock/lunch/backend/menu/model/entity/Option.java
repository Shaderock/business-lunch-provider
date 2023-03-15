package com.shaderock.lunch.backend.menu.model.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.DELETED_FILTER;

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
import jakarta.persistence.Table;
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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "options")
@SQLDelete(sql = "UPDATE options SET deleted = true WHERE id=?")
@FilterDef(name = DELETED_FILTER, parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = DELETED_FILTER, condition = "deleted = :isDeleted")
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column
  private String name;
  @Column
  private Double price;
  @Column
  private boolean deleted;
  @Column(nullable = false)
  private boolean isPublic = false;
  @ManyToMany(mappedBy = "options", fetch = FetchType.LAZY)
  @Exclude
  private Set<EmployeeOrder> employeesOrders;

  @Exclude
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @Exclude
  private List<OptionDescription> optionDescriptions;
}
