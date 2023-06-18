package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/notify")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("search")
    public List<NotificationRes> searchNotify() {
        return notificationService.searchNotify();
    }
}
