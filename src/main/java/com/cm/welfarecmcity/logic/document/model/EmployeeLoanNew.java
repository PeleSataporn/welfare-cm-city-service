package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoanNew {

  private Long empId;
  private String departmentName = null;
  private String employeeCode = null;
  private String fullName = null;
  private String employeeTypeId = null;
  private String employeeTypeName = null;
  private String stockAccumulate = null;
  private String loanValue = null;
  private String loanBalance = null;
  private String interestPercent = null;
  private String salary = null;

  // --------- loan -----------
  private Long loanId;
  private int loanTime;
  private String interestLoan;
  private String guarantorOne;
  private String guarantorTwo;
  private boolean loanActive;
  // เอาไว้เชคตอนใช้หุ้นค้ำ
  private String guaranteeStock;
  private String stockValue;
  private int installment;

  // --------- loan detail-----------
  private String loanOrdinary = null;
  private String interestLoanLastMonth = null;
  private String loanYear = null;
  private String loanMonth = null;
  private String startDateLoan = null;
}
