package com.cm.welfarecmcity.api.employee.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStatusIsActiveReq {

  private Long id;
  private String guarantorOne;
  private String guarantorTwo;
  private boolean guaranteeStockFlag;
}
