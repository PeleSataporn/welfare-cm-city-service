package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportReq {

  private String monthCurrent;
  private String yearCurrent;
  private String empCode;
  private Long stockId;
  private Long empId;
  private Long loanId;
}
