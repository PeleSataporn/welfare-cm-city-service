package com.cm.welfarecmcity.api.notification;

import java.util.List;

import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  public List<PetitionNotificationDto> searchNotify() {
    return notificationRepository.findAll();
  }
}
