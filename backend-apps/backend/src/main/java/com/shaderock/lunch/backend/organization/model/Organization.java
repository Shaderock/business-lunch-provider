package com.shaderock.lunch.backend.organization.model;

import com.shaderock.lunch.backend.user.model.entity.AppUser;
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
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Where(clause = "is_deleted=false")
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
  private boolean isDeleted;

  @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
  @Exclude
  private Set<AppUser> users;

  @OneToMany(mappedBy = "organizationRequest", fetch = FetchType.LAZY)
  @Exclude
  private Set<AppUser> usersRequests;
}
