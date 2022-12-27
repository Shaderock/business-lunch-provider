package com.shaderock.backend.model.entity.organization;

import com.shaderock.backend.model.entity.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Organization {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String name;
  @Column
  private String description;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String phone;
  @Lob
  @Column
  private byte[] logo;
  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean deleted;
  @Column
  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
  private Set<AppUser> users;
  @Column
  @OneToMany(mappedBy = "organizationRequest", fetch = FetchType.LAZY)
  private Set<AppUser> usersRequests;
}
