package com.cm.welfarecmcity.api.loandetail.model;

import lombok.Data;

@Data
public class SumLoanHistoryV2Res {

  // ดอกเบี้ยสะสม
  private String cumulativeInterest;
  // ปนผลดอกเบี้ย
  private String interestDividend;
  private Integer interestDividendIn;
}
