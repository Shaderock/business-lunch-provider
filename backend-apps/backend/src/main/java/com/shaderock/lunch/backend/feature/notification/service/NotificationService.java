package com.shaderock.lunch.backend.feature.notification.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.entity.NotificationDto;
import com.shaderock.lunch.backend.feature.notification.mapper.NotificationMapper;
import com.shaderock.lunch.backend.feature.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  public List<Notification> create(@NonNull Notification notification,
      List<AppUserDetails> appUserDetailsList, boolean excludePrincipal, Set<Role> roles) {

    appUserDetailsList = appUserDetailsList.stream()
        .filter(userDetails -> userDetails.getRoles().stream().anyMatch(roles::contains))
        .toList();

    return create(notification, appUserDetailsList, excludePrincipal);
  }

  @Transactional
  public List<Notification> create(@NonNull Notification notification,
      List<AppUserDetails> appUserDetailsList, boolean excludePrincipal) {

    if (excludePrincipal) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      appUserDetailsList = appUserDetailsList.stream()
          .filter(userDetails -> !Objects.equals(userDetails.getEmail(), authentication.getName()))
          .toList();
    }

    return create(notification, appUserDetailsList);
  }

  @Transactional
  public List<Notification> create(@NonNull Notification notification,
      List<AppUserDetails> appUserDetails) {
    return appUserDetails.stream()
        .map(userDetails -> create(Notification.builder()
            .appUser(userDetails.getAppUser())
            .content(notification.getContent())
            .build()))
        .toList();
  }

  @Transactional
  public Notification create(@NonNull Notification notification) {
    return notificationRepository.save(notification);
  }

  @Transactional
  public Notification update(@NonNull NotificationDto notificationDto,
      @NonNull AppUserDetails userDetails) {
    Notification mappedNotification = notificationMapper.toEntity(notificationDto);
    read(mappedNotification, userDetails);
    return update(mappedNotification);
  }

  @Transactional
  public Notification update(@NonNull Notification notification) {
    Notification persisted = read(notification.getId());
    persisted.setSentByBrowser(notification.isSentByBrowser());
    persisted.setViewed(notification.isViewed());
    return notificationRepository.save(persisted);
  }

  public Notification read(@NonNull UUID id) {
    return notificationRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(String.format("Notification(id=[%s]) not found", id)));
  }

  public List<Notification> read(@NonNull AppUserDetails userDetails) {
    return notificationRepository.findByAppUser_UserDetails(userDetails);
  }

  public Notification read(@NonNull Notification notification,
      @NonNull AppUserDetails userDetails) {
    return notificationRepository.findByAppUser_UserDetailsAndId(userDetails, notification.getId())
        .orElseThrow(() -> new CrudValidationException(
            String.format("Notification(id=[%s]) not found for User(email=[%s])",
                notification.getId(), userDetails.getEmail())));
  }

  public List<Notification> read(Optional<UUID> lastNotificationId, AppUserDetails userDetails) {
    if (lastNotificationId.isEmpty()) {
      return read(userDetails);
    }

    Notification lastNotification = read(lastNotificationId.get());
    return notificationRepository.findByCreatedAtGreaterThan(lastNotification.getCreatedAt(),
        Sort.by("createdAt"));
  }

  @Transactional
  public void delete(@NonNull UUID id) {
    Notification notification = read(id);
    delete(notification);
  }

  @Transactional
  public void delete(@NonNull Notification notification) {
    notificationRepository.delete(notification);
  }
}
