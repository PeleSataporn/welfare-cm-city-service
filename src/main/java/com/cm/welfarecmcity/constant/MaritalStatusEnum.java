package com.cm.welfarecmcity.constant;

import lombok.Getter;
import lombok.Setter;

public enum MaritalStatusEnum {
  SINGLE(1),
  MARRIED(2),
  WIDOWED(3),
  DIVORCE(4),
  SEPARATED(5);

  private final int state;

  private MaritalStatusEnum(int state) {
    this.state = state;
  }

  @Getter
  @Setter
  public static class MaritalStatusDto {

    private String name;
    private int state;

    MaritalStatusDto(String name, int state) {
      this.name = name;
      this.state = state;
    }
  }
}
