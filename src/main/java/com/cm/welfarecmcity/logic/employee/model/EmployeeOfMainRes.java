package com.cm.welfarecmcity.logic.employee.model;

import java.sql.Blob;
import lombok.Data;

@Data
public class EmployeeOfMainRes {

  private Long id;
  private String employeeCode = null;
  private String prefix = null;
  private String firstName = null;
  private String lastName = null;
  private String gender = null;
  private double salary = 0.0;
  private String positionName = null;
  private double stockAccumulate = 0;
  private double loanValue = 0.0;
  private double loanBalance = 0.0;
  private String departmentName = null;
  private int guarantee = 0;
  private Long profileImgId;
}
