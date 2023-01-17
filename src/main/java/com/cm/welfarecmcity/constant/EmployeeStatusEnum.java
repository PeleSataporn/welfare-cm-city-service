package com.cm.welfarecmcity.constant;

import lombok.Getter;
import lombok.Setter;

public enum EmployeeStatusEnum {
  // สถานะสมาชิกแรกเข้า
  NEW_EMPLOYEE(1),
  // สถานะคงสภาพการเป็นสมาชิก
  EMPLOYEE(2),
  // สถานะพ้นสภาพการเป็นสมาชิก
  LAYOFF(3);

  private final int state;

  private EmployeeStatusEnum(int state) {
    this.state = state;
  }

  @Getter
  @Setter
  public static class EmployeeStatusDto {

    private String name;
    private int state;

    EmployeeStatusDto(String name, int state) {
      this.name = name;
      this.state = state;
    }
  }
}
