package com.cm.welfarecmcity.logic.document.model;

import lombok.Data;

import java.util.Date;

@Data
public class DocumentInfoAllLoanEmpRes {

  private Long id = null;
  private String employeeCode = null;
  private String fullName = null;
  private Date regisDate = null;
  private String departmentName = null;
  private String employeeTypeName = null;
  // loan all
  private Long loanId = null;
  private int installment = 0;
  private String interestLastMonth = null;
  private Double loanBalance;
  private Boolean newLoan;
  private Double loanValue = null;
  private String loanTime;
  private Double interestPercent = null;
  private String guarantor1;
  private String guarantor2;
  private String startLoanDate;
  private String loanMonth;
  private String loanYear;
  private int loanOrdinary;
  private Double loanBalanceDetail;
  private int interest = 0;
  private String loanNo;
  private int principal = 0;
  private int sumOrdinary = 0;

}
