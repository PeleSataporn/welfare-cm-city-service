package com.cm.welfarecmcity.logic.document.model;

import com.cm.welfarecmcity.dto.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentLoanDetailHistory extends BaseDto {

  private int installment;
  private String loanMonth;
  private String loanYear;
  private int loanOrdinary;
  private int interest;
  private int interestPercent;
  private int interestLastMonth;
  private double loanBalance;
  private Long loanId;
  private Long employeeId;
  private String employeeCode;
  private String loanNo;
}
