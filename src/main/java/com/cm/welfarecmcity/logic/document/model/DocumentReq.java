package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentReq {

  private Long stockId = null;
  private Long loanId = null;
  private String monthCurrent;

}
