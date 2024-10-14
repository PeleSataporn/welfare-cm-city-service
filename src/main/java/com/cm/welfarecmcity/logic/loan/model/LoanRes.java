package com.cm.welfarecmcity.logic.loan.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRes {
  private Long id;
  private int loanValue;
  private int loanBalance;
  private int loanTime;
  private int stockAccumulate;
  private String loanNo;
  private String employeeCode;
  private String employeeStatus;
  private String firstName;
  private String lastName;
  private String idCard;
  private Boolean stockFlag;
  private String startLoanDate;
  private Long guarantorOne;
  private Long guarantorTwo;
  private int loanOrdinary;
  private int interestPercent;
  private String prefix;
  private String guarantorOneValue;
  private String guarantorTwoValue;
  private int interestDetail;
  private int loanBalanceDetail;
  private String loanMonth;
  private String loanYear;
  private int installment;
}
