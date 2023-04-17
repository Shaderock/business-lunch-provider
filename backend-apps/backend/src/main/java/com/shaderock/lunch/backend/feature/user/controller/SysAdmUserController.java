package com.shaderock.lunch.backend.feature.user.controller;

import com.shaderock.lunch.backend.feature.user.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
import com.shaderock.lunch.backend.feature.user.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.user.service.AppUserService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SYS_ADM_USER)
public class SysAdmUserController {

  private final AppUserService appUserService;
  private final AppUserDetailsService appUserDetailsService;

  @GetMapping
  public ResponseEntity<List<AppUserDto>> readUsers() {
    return ResponseEntity.ok(appUserService.readAllAsDTO());
  }

  @GetMapping("/details")
  public ResponseEntity<List<AppUserDetailsDto>> readUsersDetails() {
    return ResponseEntity.ok(appUserDetailsService.readAllAsDto());
  }
}
