package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
  private final AppUserDetailsService userDetailsService;

  @Transactional
  public AppUser getUserProfile(Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    return userDetails.getAppUser();
  }

  @Transactional
  public EmployeePreferenceConfig getUserProfilePreferences(Principal principal) {
    return getUserProfile(principal).getPreferences();
  }
}
