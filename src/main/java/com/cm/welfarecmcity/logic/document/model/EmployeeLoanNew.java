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
  private Long stockDetailInstallment = null;

  // --------- loan -----------
  private Long loanId = null;
  private Long loanTime = null;
  private String interestLoan = null;
  private String guarantorOne = null;
  private String guarantorTwo = null;
  private Boolean loanActive = null;
  private Boolean guaranteeStockFlag = null;
  // เอาไว้เชคตอนใช้หุ้นค้ำ
  private String guaranteeStock = null;
  private String stockValue = null;
  private Long installment = null;
  private Boolean newLoan = null;

  // --------- loan detail-----------
  private String loanOrdinary = null;
  private String interestLoanLastMonth = null;
  private String loanYear = null;
  private String loanMonth = null;
  private String startDateLoan = null;

  // --------- stock -----------
  private Long stockId = null;
  //  private Long stockValue = null;
}
