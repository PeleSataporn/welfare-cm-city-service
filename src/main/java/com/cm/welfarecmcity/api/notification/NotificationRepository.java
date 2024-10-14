package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository
    extends JpaRepository<PetitionNotificationDto, Long>,
        JpaSpecificationExecutor<PetitionNotificationDto> {}
