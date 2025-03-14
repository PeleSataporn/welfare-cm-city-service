package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notify")
public class NotificationController {

  @Autowired private NotificationService notificationService;

  @PostMapping("search")
  public List<NotificationRes> searchNotify() {
    return notificationService.searchNotify();
  }

  @PostMapping("getNotifyByEmpId")
  public List<NotificationRes> getNotifyByEmpId(@RequestBody NotificationRes req) {
    return notificationService.getNotifyByEmpId(req);
  }

  @DeleteMapping("/cancel/{id}/{empId}")
  public void cancel(@PathVariable Long id, @PathVariable Long empId) {
    notificationService.cancel(id, empId);
  }

  @DeleteMapping("/reject-register/{id}/{empId}")
  public void rejectRegister(@PathVariable Long id, @PathVariable Long empId) {
    notificationService.rejectRegister(id, empId);
  }

  @DeleteMapping("/delete-notify/{id}")
  public void deleteNotify(@PathVariable Long id) {
    notificationService.deleteNotify(id);
  }
}
