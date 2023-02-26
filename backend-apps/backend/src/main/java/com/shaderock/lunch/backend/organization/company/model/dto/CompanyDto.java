package com.shaderock.lunch.backend.organization.company.model.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.organization.company.model.entity.Company}
 * entity
 */
public record CompanyDto(Long id,
                         String name,
                         String description,
                         String email,
                         String phone,
                         byte[] logo,
                         Set<Long> usersIds,
                         Set<Long> usersRequestsIds,
                         Set<Long> subscriptionsIds,
                         Set<Long> subscriptionsRequestsIds,
                         Long preferencesId) implements Serializable {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CompanyDto that = (CompanyDto) o;

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    if (!Objects.equals(description, that.description)) {
      return false;
    }
    if (!Objects.equals(email, that.email)) {
      return false;
    }
    if (!Objects.equals(phone, that.phone)) {
      return false;
    }
    if (!Arrays.equals(logo, that.logo)) {
      return false;
    }
    if (!Objects.equals(usersIds, that.usersIds)) {
      return false;
    }
    if (!Objects.equals(usersRequestsIds, that.usersRequestsIds)) {
      return false;
    }
    if (!Objects.equals(subscriptionsIds, that.subscriptionsIds)) {
      return false;
    }
    if (!Objects.equals(subscriptionsRequestsIds, that.subscriptionsRequestsIds)) {
      return false;
    }
    return Objects.equals(preferencesId, that.preferencesId);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (phone != null ? phone.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(logo);
    result = 31 * result + (usersIds != null ? usersIds.hashCode() : 0);
    result = 31 * result + (usersRequestsIds != null ? usersRequestsIds.hashCode() : 0);
    result = 31 * result + (subscriptionsIds != null ? subscriptionsIds.hashCode() : 0);
    result =
        31 * result + (subscriptionsRequestsIds != null ? subscriptionsRequestsIds.hashCode() : 0);
    result = 31 * result + (preferencesId != null ? preferencesId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CompanyDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", email='" + email + '\'' +
        ", phone='" + phone + '\'' +
        ", logo=" + Arrays.toString(logo) +
        ", usersIds=" + usersIds +
        ", usersRequestsIds=" + usersRequestsIds +
        ", subscriptionsIds=" + subscriptionsIds +
        ", subscriptionsRequestsIds=" + subscriptionsRequestsIds +
        ", preferencesId=" + preferencesId +
        '}';
  }
}
