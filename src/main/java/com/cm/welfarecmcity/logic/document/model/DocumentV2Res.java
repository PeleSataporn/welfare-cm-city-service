package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentV2Res {

  private String departmentName;
  private String stockValueTotal;
  private String stockAccumulateTotal;
  private String totalMonth;
  private String loanDetailOrdinaryTotal;
  private String loanDetailInterestTotal;
}
