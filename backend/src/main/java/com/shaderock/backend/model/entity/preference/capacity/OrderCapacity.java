package com.shaderock.backend.model.entity.preference.capacity;

import com.shaderock.backend.model.entity.preference.SupplierPreferenceConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderCapacity implements Serializable {
  @Serial
  private static final long serialVersionUID = 8L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "preference_config_id", nullable = false)
  private SupplierPreferenceConfig preferenceConfig;
  private Duration duration;
  private int employeesOrdersAmount;
}
