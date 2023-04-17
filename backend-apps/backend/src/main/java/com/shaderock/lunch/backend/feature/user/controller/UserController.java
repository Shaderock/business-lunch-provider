package com.shaderock.lunch.backend.feature.user.controller;

import com.shaderock.lunch.backend.feature.config.preference.employee.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.config.preference.employee.mapper.EmployeePreferencesMapper;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.feature.user.mapper.AppUserMapper;
import com.shaderock.lunch.backend.feature.user.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.user.service.AppUserService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.USER)
public class UserController {

  private final AppUserService appUserService;
  private final EmployeePreferencesMapper employeePreferencesMapper;
  private final AppUserMapper appUserMapper;
  private final AppUserDetailsService appUserDetailsService;
  private final AppUserDetailsMapper appUserDetailsMapper;

  @GetMapping("/profile")
  public ResponseEntity<AppUserDto> readUser(Principal principal) {
    AppUser user = appUserService.readProfile(principal);
    return ResponseEntity.ok(appUserMapper.toDto(user));
  }

  @GetMapping("/profile/details")
  public ResponseEntity<AppUserDetailsDto> readUserDetails(Principal principal) {
    AppUserDetails user = appUserDetailsService.readProfile(principal);
    return ResponseEntity.ok(appUserDetailsMapper.toDto(user));
  }

  @GetMapping("/profile/preferences")
  public ResponseEntity<EmployeePreferencesDto> getProfilePreferences(Principal principal) {
    EmployeePreferences preferences = appUserService.getUserProfilePreferences(principal);
    return ResponseEntity.ok(employeePreferencesMapper.toDto(preferences));
  }
}