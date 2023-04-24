package com.shaderock.lunch.backend.feature.organization.entity;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
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
@Entity
@SQLDelete(sql = "UPDATE organization_details SET is_deleted = true WHERE id=?")
public class OrganizationDetails extends DeletableEntity {

  @Column(nullable = false)
  private String name;
  @Column(length = 1024)
  private String description;
  @Column
  private String email;
  @Column
  private String phone;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(length = 2048)
  @Exclude
  private byte[] logo;

  @OneToMany(mappedBy = "organizationDetails", fetch = FetchType.LAZY)
  @Exclude
  private List<AppUser> users;

  @Builder
  public OrganizationDetails(UUID id, String name, String description,
      String email, String phone, List<AppUser> users) {
    super(id);
    this.name = name;
    this.description = description;
    this.email = email;
    this.phone = phone;
    this.users = users;
  }
}
