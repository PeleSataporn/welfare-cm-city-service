package com.cm.welfarecmcity.api.notification;

import java.util.List;

import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Transactional
  public List<PetitionNotificationDto> searchNotify() {
    return notificationRepository.findAll();
  }
}
