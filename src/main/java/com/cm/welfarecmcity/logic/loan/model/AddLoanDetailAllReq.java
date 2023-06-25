package com.cm.welfarecmcity.logic.loan.model;

import lombok.Data;

@Data
public class AddLoanDetailAllReq {

  // old
  private String oldMonth;
  private String oldYear;
  // new
  private String newMonth;
  private String newYear;
}
