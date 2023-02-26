package com.shaderock.lunch.backend.user.model.entity;

import static com.shaderock.lunch.backend.utils.FilterManager.DELETED_FILTER;

import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "app_users_details")
@SQLDelete(sql = "UPDATE app_users_details SET deleted = true WHERE id=?")
@FilterDef(name = DELETED_FILTER, parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = DELETED_FILTER, condition = "deleted = :isDeleted")
public class AppUserDetails implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_user_id")
  @Exclude
  private AppUser appUser;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;

  @Column
  private String registrationToken;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isEnabled;

  @ElementCollection(targetClass = Role.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role", nullable = false)
  private Set<Role> roles;

  @Override
  @Transactional
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getNameWithPrefix()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
