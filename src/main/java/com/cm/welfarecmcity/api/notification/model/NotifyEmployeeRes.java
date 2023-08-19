package com.cm.welfarecmcity.api.notification.model;

import lombok.Data;

@Data
public class NotifyEmployeeRes {

  private Long id;
  private String employeeCode;
  private String prefix;
  private String firstName;
  private String lastName;
  private String idCard;
  private String gender;
  private String positionName;
  private String affiliationName;
  private String employeeTypeName;
  private String departmentName;
  private String levelName = null;
  private String bureauName;
  private String marital;
  private int stockAccumulate;
  private double loanBalance;
  private Long profileImgId;
}
