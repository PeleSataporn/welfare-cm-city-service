package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.register.model.req.CancelRegisterReq;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notify")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  @PostMapping("search")
  public List<NotificationRes> searchNotify() {
    return notificationService.searchNotify();
  }

  @DeleteMapping("/cancel/{id}/{empId}")
  public void cancel(@PathVariable Long id, @PathVariable Long empId) {
    notificationService.cancel(id, empId);
  }
}
