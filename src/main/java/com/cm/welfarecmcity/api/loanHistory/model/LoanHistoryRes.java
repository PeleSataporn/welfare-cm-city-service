package com.cm.welfarecmcity.api.loanHistory.model;

import lombok.Data;

@Data
public class LoanHistoryRes {
  private Long empId;
  private Long loanId;
  private String status;
}
