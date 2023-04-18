package com.shaderock.lunch.backend.feature.details.controller;

import com.shaderock.lunch.backend.feature.details.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
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
@RequestMapping(ApiConstants.USER_DETAILS)
public class UserDetailsController {

  private final AppUserDetailsService appUserDetailsService;
  private final AppUserDetailsMapper appUserDetailsMapper;

  @GetMapping("/profile")
  public ResponseEntity<AppUserDetailsDto> readUserDetails(Principal principal) {
    AppUserDetails user = appUserDetailsService.read(principal);
    return ResponseEntity.ok(appUserDetailsMapper.toDto(user));
  }
}
