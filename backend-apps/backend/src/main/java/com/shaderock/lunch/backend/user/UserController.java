package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.user.model.UserDTO;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.preferences.EmployeePreferencesDtoConverter;
import com.shaderock.lunch.backend.user.preferences.model.EmployeePreferencesDTO;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  private final UserDtoConverter userDtoConverter;
  private final EmployeePreferencesDtoConverter employeePreferencesDtoConverter;

  @GetMapping("/profile")
  public ResponseEntity<UserDTO> getProfile(Principal principal) {
    AppUser user = userService.getUserProfile(principal);
    return ResponseEntity.ok(userDtoConverter.convertToDto(user));

  }

  @GetMapping("/profile/preferences")
  public ResponseEntity<EmployeePreferencesDTO> getProfilePreferences(Principal principal) {
    EmployeePreferenceConfig preferences = userService.getUserProfilePreferences(principal);
    return ResponseEntity.ok(employeePreferencesDtoConverter.convertToDto(preferences));
  }
}