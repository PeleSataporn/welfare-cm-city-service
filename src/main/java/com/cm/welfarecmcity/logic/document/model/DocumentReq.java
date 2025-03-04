package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentReq {

  private Long empId = null;
  private Long stockId = null;
  private Long loanId = null;
  private String monthCurrent;
  private String yearCurrent;
  private String yearOld;
  private String empCode;
  private String fullName;

  // ปนผลหุน %
  private String stockDividendPercent;
  // ปนผลดอกเบี้ย %
  private String interestDividendPercent;

  private Boolean admin;
  private Boolean passwordFlag;
}
