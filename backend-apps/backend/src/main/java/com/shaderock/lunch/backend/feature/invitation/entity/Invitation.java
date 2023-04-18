package com.shaderock.lunch.backend.feature.invitation.entity;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Invitation extends BaseEntity {

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_user_id")
  private AppUser appUser;

  @Builder
  public Invitation(UUID id, LocalDateTime createdAt, Company company, AppUser appUser) {
    super(id);
    this.createdAt = createdAt;
    this.company = company;
    this.appUser = appUser;
  }

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}
