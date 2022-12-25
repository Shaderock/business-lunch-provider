package com.shaderock.backend.auth.login.model;

import com.shaderock.backend.model.type.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDTO {
  private String email;
  private String token;
  private String firstName;
  private String lastName;
  private Set<Role> roles;
}
