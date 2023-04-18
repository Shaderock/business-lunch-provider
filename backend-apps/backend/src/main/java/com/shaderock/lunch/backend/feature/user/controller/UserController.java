package com.shaderock.lunch.backend.feature.user.controller;

import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.mapper.AppUserMapper;
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
  private final AppUserMapper appUserMapper;

  @GetMapping("/profile")
  public ResponseEntity<AppUserDto> read(Principal principal) {
    AppUser user = appUserService.read(principal);
    return ResponseEntity.ok(appUserMapper.toDto(user));
  }
}
