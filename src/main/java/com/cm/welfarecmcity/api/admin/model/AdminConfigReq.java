package com.cm.welfarecmcity.api.admin.model;

import lombok.Data;

@Data
public class AdminConfigReq {
  private Long configId;
  private String name;
  private String value;
  private String description;
  private String monthCurrent;
  private String yearCurrent;
  private String paymentStartDate;

  // emp
  private Long empId;
  private String employeeCode;
  private Boolean adminFlag;
}
