package com.cm.welfarecmcity.logic.register.model.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmployeeCodeRes {
  private Long id;
  private String employeeCode;
  private String idCard;
  private int employeeStatus;
}
