package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrandTotalRes {

  private Long sumEmp;
  private Long sumLoan;
  private Long sumLoanBalance;
  private Long sumStockAccumulate;
  private Long sumStockValue;
  private Long sumLoanInterest;
  private Long sumLoanOrdinary;
  private Long sumTotal;
}
