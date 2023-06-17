package com.cm.welfarecmcity.logic.stock.model;

import lombok.Data;

@Data
public class AddStockDetailAllReq {

  // old
  private String oldMonth;
  private String oldYear;
  // new
  private String newMonth;
  private String newYear;
}
