package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
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
  private EmployeeRepository employeeRepository;

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

      if (petitionNotification.getEmployee().getEmployeeType() == null) {
        notification.getEmployee().setEmployeeTypeName(null);
      } else {
        notification.getEmployee().setEmployeeTypeName(petitionNotification.getEmployee().getEmployeeType().getName());
      }

      notification.getEmployee().setDepartmentName(petitionNotification.getEmployee().getDepartment().getName());

      if (petitionNotification.getEmployee().getLevel() == null) {
        notification.getEmployee().setLevelName(null);
      } else {
        notification.getEmployee().setLevelName(petitionNotification.getEmployee().getLevel().getName());
      }

      if (petitionNotification.getEmployee().getStock() == null) {
        notification.getEmployee().setStockAccumulate(0);
      } else {
        notification.getEmployee().setStockAccumulate(petitionNotification.getEmployee().getStock().getStockAccumulate());
      }

      if (petitionNotification.getEmployee().getLoan() == null) {
        notification.getEmployee().setLoanBalance(0);
      } else {
        notification.getEmployee().setLoanBalance(petitionNotification.getEmployee().getLoan().getLoanBalance());
      }

      notification.getEmployee().setBureauName(petitionNotification.getEmployee().getAffiliation().getBureau().getName());

      notifications.add(notification);
    }

    return notifications;
  }

  @Transactional
  public void cancel(Long id, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    emp.setCheckStockValueFlag(false);
    employeeRepository.save(emp);

    val notification = notificationRepository.findById(id).get();
    notificationRepository.delete(notification);
  }

  @Transactional
  public void deleteNotify(Long id) {
    val notification = notificationRepository.findById(id).get();
    notificationRepository.delete(notification);
  }
}
