package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentLoanV1Res {

  private String loanInstallment = null;
  private String loanOrdinary = null;
  private String interest = null;
  private String loanTime = null;
  private Boolean loanActive = null;
  private Boolean loanDelete = null;
  private Date closeLoanDate;
}
