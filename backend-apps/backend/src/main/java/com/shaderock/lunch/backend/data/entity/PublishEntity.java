package com.shaderock.lunch.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PublishEntity extends VisibleEntity {

  @Column
  private LocalDate createdAt;

  @Column
  private LocalDate publishedAt;

  public PublishEntity(boolean isPublic, LocalDate createdAt, LocalDate publishedAt) {
    super(isPublic);
    this.createdAt = createdAt;
    this.publishedAt = publishedAt;
  }

  public PublishEntity(UUID id, boolean isDeleted, boolean isPublic, LocalDate createdAt,
      LocalDate publishedAt) {
    super(id, isDeleted, isPublic);
    this.createdAt = createdAt;
    this.publishedAt = publishedAt;
  }

  public PublishEntity(UUID id, boolean isPublic, LocalDate createdAt, LocalDate publishedAt) {
    super(id, isPublic);
    this.createdAt = createdAt;
    this.publishedAt = publishedAt;
  }
}

