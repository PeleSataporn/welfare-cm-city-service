package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentV1Res {
  private Long empId = null;
  private String departmentName = null;
  private String employeeCode = null;
  private String fullName = null;
  private String stockInstallment = null;
  private String stockValue = null;
  private String loanInstallment = null;
  private String loanOrdinary = null;
  private String interest = null;
  private String sumMonth = null;
  private String stockAccumulate = null;
  private String loanTime = null;
  private Long loanId = null;
}
