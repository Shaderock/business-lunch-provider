package com.shaderock.lunch.backend.feature.notification.controller;

import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.entity.NotificationDto;
import com.shaderock.lunch.backend.feature.notification.mapper.NotificationMapper;
import com.shaderock.lunch.backend.feature.notification.service.NotificationService;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.service.AppUserDetailsService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.NOTIFICATIONS)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationMapper notificationMapper;
  private final AppUserDetailsService userDetailsService;
  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> read(
      @RequestParam(required = false) Optional<UUID> lastNotificationId, Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    List<Notification> notifications = notificationService.read(lastNotificationId, userDetails);
    return ResponseEntity.ok(notifications.stream().map(notificationMapper::toDto).toList());
  }

  @PutMapping
  public ResponseEntity<NotificationDto> update(
      @RequestBody @NonNull NotificationDto notificationDto, Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Notification updated = notificationService.update(notificationDto, userDetails);
    return ResponseEntity.ok(notificationMapper.toDto(updated));
  }

}
