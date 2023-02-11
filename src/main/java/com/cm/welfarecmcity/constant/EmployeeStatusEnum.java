package com.cm.welfarecmcity.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public enum EmployeeStatusEnum {
  // สมาชิกแรกเข้า (สมัครเข้าใช้งานใหม่)
  NEW_EMPLOYEE(1),
  // ใช้งานปกติ ( ปัจจุบัน )
  NORMAL_EMPLOYEE(2),
  // ลาออก
  RESIGN_EMPLOYEE(3);

  private final int state;

  private EmployeeStatusEnum(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  @Data
  @AllArgsConstructor
  public static class EmployeeStatusDto {

    private String name;
    private int state;
  }
}
