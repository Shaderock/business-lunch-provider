package com.shaderock.lunch.backend.order.model.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private boolean isDeleted;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private Set<Option> options;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id", nullable = false)
  private Menu menu;
}
