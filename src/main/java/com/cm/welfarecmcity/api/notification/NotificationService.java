package com.cm.welfarecmcity.api.notification;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.api.stockdetail.StockDetailRepository;
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

  @Autowired private NotificationRepository notificationRepository;

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private NotificationLogicRepository notificationLogicRepository;

  @Autowired private MapStructMapper mapStructMapper;

  @Autowired private StockRepository stockRepository;

  @Autowired private StockDetailRepository stockDetailRepository;

  @Transactional
  public List<NotificationRes> searchNotify() {
    val listNotification = notificationRepository.findAll();

    List<NotificationRes> notifications = new ArrayList<>();

    for (PetitionNotificationDto petitionNotification : listNotification) {
      val notification = mapStructMapper.notificationToRes(petitionNotification);

      if (petitionNotification.getEmployee().getPosition() == null) {
        notification.getEmployee().setPositionName(null);
      } else {
        notification
            .getEmployee()
            .setPositionName(petitionNotification.getEmployee().getPosition().getName());
      }

      if (petitionNotification.getEmployee().getAffiliation() == null) {
        notification.getEmployee().setAffiliationName(null);
      } else {
        notification
            .getEmployee()
            .setAffiliationName(petitionNotification.getEmployee().getAffiliation().getName());
      }

      if (petitionNotification.getEmployee().getEmployeeType() == null) {
        notification.getEmployee().setEmployeeTypeName(null);
      } else {
        notification
            .getEmployee()
            .setEmployeeTypeName(petitionNotification.getEmployee().getEmployeeType().getName());
      }

      if (petitionNotification.getEmployee().getDepartment() == null) {
        notification.getEmployee().setDepartmentName(null);
      } else {
        notification
            .getEmployee()
            .setDepartmentName(petitionNotification.getEmployee().getDepartment().getName());
      }

      if (petitionNotification.getEmployee().getLevel() == null) {
        notification.getEmployee().setLevelName(null);
      } else {
        notification
            .getEmployee()
            .setLevelName(petitionNotification.getEmployee().getLevel().getName());
      }

      if (petitionNotification.getEmployee().getStock() == null) {
        notification.getEmployee().setStockAccumulate(0);
      } else {
        notification
            .getEmployee()
            .setStockAccumulate(petitionNotification.getEmployee().getStock().getStockAccumulate());
      }

      if (petitionNotification.getEmployee().getLoan() == null) {
        notification.getEmployee().setLoanBalance(0);
      } else {
        notification
            .getEmployee()
            .setLoanBalance(petitionNotification.getEmployee().getLoan().getLoanBalance());
      }

      if (petitionNotification.getEmployee().getAffiliation() == null) {
        notification.getEmployee().setBureauName(null);
      } else {
        notification
            .getEmployee()
            .setBureauName(
                petitionNotification.getEmployee().getAffiliation().getBureau().getName());
      }

      if (petitionNotification.getEmployee().getMarital() == null) {
        notification.getEmployee().setMarital(null);
      } else {
        notification.getEmployee().setMarital(petitionNotification.getEmployee().getMarital());
      }

      if (petitionNotification.getEmployee().getProfileImg() == null) {
        notification.getEmployee().setProfileImgId(null);
      } else {
        notification
            .getEmployee()
            .setProfileImgId(petitionNotification.getEmployee().getProfileImg().getId());
      }

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

  @Transactional
  public List<NotificationRes> getNotifyByEmpId(NotificationRes req) {
    val notifyDataList = notificationLogicRepository.getNotificationByEmpId(req.getEmployeeId());
    if (notifyDataList != null && notifyDataList.size() > 0) {
      return notifyDataList;
    } else {
      return null;
    }
  }

  @Transactional
  public void rejectRegister(Long id, Long empId) {
    val notification =
        notificationRepository.findById(id).get(); //    employeeRepository.save(emp);
    notificationRepository.delete(notification);
    //    val emp = employeeRepository.findById(empId).get();
    //
    //    val stock = new StockDto();
    //    stock.setId(emp.getStock().getId());
    //
    //    val stockDetail = stockDetailRepository.findAllByStock_Id(emp.getStock().getId()).get(0);
    //    stockDetailRepository.delete(stockDetail);
    //
    //    employeeRepository.deleteById(emp.getId());
    //
    //    val stockTemp = stockRepository.findById(stock.getId()).get();
    //    stockRepository.delete(stockTemp);
  }
}
