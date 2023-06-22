package com.cm.welfarecmcity.logic.loan.model;

import lombok.Data;

@Data
public class GuarantorRes {

  // One
  private String genderOne;
  private String codeGuarantorOne = null;
  private String fullNameGuarantorOne = null;
  // Two
  private String genderTwo;
  private String codeGuarantorTwo = null;
  private String fullNameGuarantorTwo = null;
}
