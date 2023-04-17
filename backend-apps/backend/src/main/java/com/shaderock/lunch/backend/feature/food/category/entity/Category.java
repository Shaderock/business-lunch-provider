package com.shaderock.lunch.backend.feature.food.category.entity;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
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
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id=?")
public class Category extends VisibleEntity {

  @Column
  private String name;
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @Exclude
  private Set<Option> options = new HashSet<>();
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_id", nullable = false)
  @Exclude
  private Menu menu;

  @Builder
  public Category(UUID id, boolean isDeleted, boolean isPublic, String name,
      Set<Option> options, Menu menu) {
    super(id, isDeleted, isPublic);
    this.name = name;
    this.options = options;
    this.menu = menu;
  }
}
