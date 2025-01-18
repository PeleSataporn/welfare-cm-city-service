package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentV2ResLoan {
  private String departmentName;
  private String loanValueTotal;
  // เดือนนี้(ดอก)
  private String monthInterestTotal;
  // เดือนนี้(ต้น)
  private String monthPrincipleTotal;
  // สุดทาย(ดอก)
  private String lastMonthInterestTotal;
  // สุดทาย(ต้น)
  private String lastMonthPrincipleTotal;
  // รวมสง(ดอก)
  private String totalValueInterestTotal;
  // คงคาง(ดอก)
  private String outStandInterestTotal;
  // รวมสง(ตน)
  private String totalValuePrincipleTotal;
  // คงคาง(ต้น)
  private String outStandPrincipleTotal;
  private Boolean loanActive = null;
  private Double loanValue = null;
  private String loanTime = null;
  private String installment = null;
  private Date closeLoanDate;
}
