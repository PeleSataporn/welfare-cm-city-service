package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRes {

  private String month = null;
  private String employeeCode = null;
  private String departmentName = null;
  private Double stockAccumulate = 0.00;
  private String fullName = null;
  private Long stockDetailInstallment = 0L;
  private Double stockValue = 0.00;
  private Long installment = 0L;
  private Long interest = 0L;
  private Double totalPrice = 0.00;
  private Double principalBalance = 0.00;
  private Double totalDeduction = 0.00;
  private String totalText = null;
  private Object signature1 = null;
  private Object signature2 = null;
}
