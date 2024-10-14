package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculateReq {

  private double principal;
  private double interestRate;
  private int numOfPayments;
  private String paymentStartDate;
}
