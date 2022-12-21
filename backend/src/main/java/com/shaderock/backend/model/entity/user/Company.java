package com.shaderock.backend.model.entity.user;

import com.shaderock.backend.model.entity.preference.CompanyPreferenceConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company implements Serializable {
  @Serial
  private static final long serialVersionUID = 3L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Email
  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String deliveryAddress;

  @Lob
  @Column
  private byte[] logo;

  @Column
  private boolean isDeleted;

  @Column
  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  private Set<Employee> employees;

  @Column
  @OneToMany(mappedBy = "companyRequest", fetch = FetchType.LAZY)
  private Set<Employee> employeesRequests;

  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  private Set<Supplier> subscriptions;

  @ManyToMany(mappedBy = "subscriptionsRequests", fetch = FetchType.LAZY)
  private Set<Supplier> subscriptionsRequest;

  @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
  private CompanyPreferenceConfig preferences;
}
