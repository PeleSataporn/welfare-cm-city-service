package com.cm.welfarecmcity.api.notification.model;

import lombok.Data;

@Data
public class NotifyEmployeeRes {

  private String employeeCode;
  private String firstName;
  private String lastName;
  private String positionName;
  private String affiliationName;
}
