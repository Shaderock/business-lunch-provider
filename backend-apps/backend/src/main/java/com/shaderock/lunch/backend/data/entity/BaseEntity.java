package com.shaderock.lunch.backend.data.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
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
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  protected UUID id;
}
