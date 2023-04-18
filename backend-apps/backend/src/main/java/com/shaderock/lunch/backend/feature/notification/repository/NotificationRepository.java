package com.shaderock.lunch.backend.feature.notification.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;

public interface NotificationRepository extends BaseEntityRepository<Notification> {

  List<Notification> findByCreatedAtGreaterThan(LocalDateTime createdAt, Sort sort);

  List<Notification> findByAppUser_UserDetails(AppUserDetails userDetails);

  Optional<Notification> findByAppUser_UserDetailsAndId(AppUserDetails userDetails, UUID id);

}
