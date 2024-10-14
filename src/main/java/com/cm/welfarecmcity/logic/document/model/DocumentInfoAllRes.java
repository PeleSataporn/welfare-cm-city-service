package com.cm.welfarecmcity.logic.document.model;

import java.util.Date;
import lombok.Data;

@Data
public class DocumentInfoAllRes {

  private Long id = null;
  private String employeeCode = null;
  private String fullName = null;
  private Date regisDate = null;
  private String departmentName = null;
  private String employeeTypeName = null;
  private String positionsName = null;
  private String salary = null;
  private Long stockId = null;
  private String stockValue = null;
  private String stockAccumulate = null;
  private int installment = 0;
  private Long loanId = null;
  private Double loanValue = null;
  private String loanTime;
  private Double interestPercent = null;
  //  Guarantor
  private String codeGuarantorOne = null;
  private String fullNameGuarantorOne = null;
  private String codeGuarantorTwo = null;
  private String fullNameGuarantorTwo = null;
  //  Guarantee
  private String codeGuaranteeOne = null;
  private String fullNameGuaranteeOne = null;
  private String codeGuaranteeTwo = null;
  private String fullNameGuaranteeTwo = null;
  // loan details
  private int interestMonth;
  private int earlyMonth;
  private int interestMonthLast;
  private int earlyMonthLast;
  private int installmentLoan;
  private int totalValueInterest;
  private int totalValuePrinciple;
  private int outStandInterest;
  private int outStandPrinciple;
  private String interestLastMonth = null;
  private Boolean newLoan;
  private String startLoanDate;
}
