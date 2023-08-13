package com.cm.welfarecmcity.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

public enum NotificationStatusEnum {
  // ลาออก
  RESIGN(1),
  // หุ้นสะสม
  STOCK_ACCUMULATE(2),
  // สมัคร
  REGISTER(3),
  // ผู้รับผลประโยชน์
  BENEFICIARY(4),
  UPDATE_BY_USER(5);

  private final int state;

  private NotificationStatusEnum(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  @Data
  @AllArgsConstructor
  public static class NotificationStatusDto {

    private String name;
    private int state;
  }
}
