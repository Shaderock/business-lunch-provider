package com.shaderock.lunch.backend.menu.model.entity;

import com.shaderock.lunch.backend.data.BaseEntity;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Menu extends BaseEntity {

  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
  @Exclude
  private Set<Category> categories;

  @OneToOne(mappedBy = "menu", optional = false)
  private Supplier supplier;
}
