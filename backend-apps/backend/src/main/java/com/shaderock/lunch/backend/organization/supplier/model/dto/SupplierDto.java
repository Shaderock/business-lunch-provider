package com.shaderock.lunch.backend.organization.supplier.model.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier}
 * entity
 */
public record SupplierDto(Long id,
                          String name,
                          String description,
                          String email,
                          String phone,
                          byte[] logo,
                          Set<Long> usersIds,
                          Set<Long> usersRequestsIds,
                          URI websiteUrl,
                          URI menuUrl,
                          Set<Long> subscribersIds,
                          Set<Long> subscriptionsRequestsIds,
                          Long menuId,
                          Long preferencesId) implements Serializable {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SupplierDto that = (SupplierDto) o;

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(name, that.name)) {
      return false;
    }
    return Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SupplierDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", email='" + email + '\'' +
        ", phone='" + phone + '\'' +
        ", logo=" + Arrays.toString(logo) +
        ", usersIds=" + usersIds +
        ", usersRequestsIds=" + usersRequestsIds +
        ", websiteUrl=" + websiteUrl +
        ", menuUrl=" + menuUrl +
        ", subscribersIds=" + subscribersIds +
        ", subscriptionsRequestsIds=" + subscriptionsRequestsIds +
        ", menuId=" + menuId +
        ", preferencesId=" + preferencesId +
        '}';
  }
}
