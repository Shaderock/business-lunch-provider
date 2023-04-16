package com.shaderock.lunch.backend.menu.model.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.SQLDelete;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE option_description SET is_deleted = true WHERE id=?")
public class SubOptions extends DeletableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "option_id", nullable = false)
  @Exclude
  private Option option;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Builder
  public SubOptions(UUID id, Option option, String name, String description) {
    super(id);
    this.option = option;
    this.name = name;
    this.description = description;
  }
}
