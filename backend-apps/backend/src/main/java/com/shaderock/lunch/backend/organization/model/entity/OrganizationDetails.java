package com.shaderock.lunch.backend.organization.model.entity;

import com.shaderock.lunch.backend.data.DeletableEntity;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@SQLDelete(sql = "UPDATE organization_details SET is_deleted = true WHERE id=?")
public class OrganizationDetails extends DeletableEntity {

  @Column(nullable = false)
  private String name;
  @Column
  private String description;
  @Column
  private String email;
  @Column
  private String phone;
  @Lob
  @Column
  private byte[] logo;

  @OneToMany(mappedBy = "organizationDetails", fetch = FetchType.LAZY)
  @Exclude
  private Set<AppUser> users;

  @OneToMany(mappedBy = "organizationDetailsRequest", fetch = FetchType.LAZY)
  @Exclude
  private Set<AppUser> usersRequests;
}
