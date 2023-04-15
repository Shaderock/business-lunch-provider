package com.shaderock.lunch.backend.menu.model.entity;

import com.shaderock.lunch.backend.data.VisibleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
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
@Builder
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
}
