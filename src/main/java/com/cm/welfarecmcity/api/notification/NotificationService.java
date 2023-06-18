package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.mapper.MapStructMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private MapStructMapper mapStructMapper;

  @Transactional
  public List<NotificationRes> searchNotify() {
    val listNotification = notificationRepository.findAll();

    List<NotificationRes> notifications = new ArrayList<>();

    for (PetitionNotificationDto petitionNotification : listNotification) {
      val notification = mapStructMapper.notificationToRes(petitionNotification);
      notification.getEmployee().setPositionName(petitionNotification.getEmployee().getPosition().getName());
      notification.getEmployee().setAffiliationName(petitionNotification.getEmployee().getAffiliation().getName());
      notifications.add(notification);
    }

    return notifications;
  }
}
