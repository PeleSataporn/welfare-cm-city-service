package com.cm.welfarecmcity.logic.document.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentInfoAllRes {

  private String employeeCode = null;
  private String fullName = null;
  private Date regisDate = null;
  private String departmentName = null;
  private String employeeTypeName = null;
  private String positionsName = null;
  private String salary = null;
  private String stockValue = null;
  private String stockAccumulate = null;
  private String loanValue = null;
  private String loanTime = null;
  private String interestPercent = null;
  private String codeGuarantorOne = null;
  private String fullNameGuarantorOne = null;
  private String codeGuarantorTwo = null;
  private String fullNameGuarantorTwo = null;
}
