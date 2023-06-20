package com.cm.welfarecmcity.api.notification.model;

import java.util.Date;
import lombok.Data;

@Data
public class NotificationRes {

  private Long id;
  private int status;
  private String reason;
  private String description;
  private Date createDate;
  private NotifyEmployeeRes employee;
}
