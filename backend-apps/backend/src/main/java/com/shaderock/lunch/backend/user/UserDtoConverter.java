package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.user.model.UserDTO;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {
  @Transactional
  public UserDTO convertToDto(AppUserDetails userDetails) {
    if (Objects.isNull(userDetails)) {
      return null;
    }
    return new UserDTO(userDetails.getAppUser().getId(), userDetails.getEmail(),
                       userDetails.getFirstName(), userDetails.getLastName(),
                       userDetails.getRoles());
  }

  @Transactional
  public UserDTO convertToDto(AppUser user) {
    return convertToDto(user.getUserDetails());
  }
}
