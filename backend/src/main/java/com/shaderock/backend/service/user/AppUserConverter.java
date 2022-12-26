package com.shaderock.backend.service.user;

import com.shaderock.backend.model.entity.user.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class AppUserConverter<U1 extends AppUser, U2 extends AppUser> {
  @Transactional
  public void convertFromTo(U1 from, U2 to) {
    to.setId(from.getId());
    to.setEmail(from.getEmail());
    to.setFirstName(from.getFirstName());
    to.setLastName(from.getLastName());
    to.setPassword(from.getPassword());
    to.setRegistrationToken(from.getRegistrationToken());
    to.setRoles(from.getRoles());
    to.setEnabled(from.isEnabled());
  }
}
