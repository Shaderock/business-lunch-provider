package com.shaderock.lunch.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Duration;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class NotificationConfig extends BaseEntity {

  @Column
  private LocalTime startTime;
  @Column
  private int amount;
  @Column
  private Duration interval;
}
