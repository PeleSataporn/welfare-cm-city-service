package com.cm.welfarecmcity.logic.employee.model;

import com.cm.welfarecmcity.dto.BeneficiaryDto;
import java.util.List;
import lombok.Data;

@Data
public class EmployeeOfMainRes {

  private String employeeCode;
  private String prefix;
  private String firstName;
  private String lastName;
  private String gender;
  private String positionName;
  private int stockAccumulate;
  private double loanValue;
  private String departmentName;
  private int guarantee;
}
