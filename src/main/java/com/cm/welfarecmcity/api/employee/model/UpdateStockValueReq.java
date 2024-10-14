package com.cm.welfarecmcity.api.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockValueReq {

  private Long id;
  private int stockValue;
  private int stockOldValue;
}
