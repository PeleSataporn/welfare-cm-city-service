package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentV1ResLoan {
    private String departmentName = null;
    private String employeeCode = null;
    private String fullName = null;
    private String loanValue = null;
    private String loanTime = null;
    private String interestPercent = null;
    private String guarantor1 = null;
    private String guarantor2 = null;
    private String guarantorCode1 = null;
    private String guarantorCode2 = null;
    private String interestLastMonth = null;
    private Boolean newLoan;
    // เดือนนี้(ดอก)
    private String monthInterest;
    // เดือนนี้(ต้น)
    private String monthPrinciple;
    // สุดทาย(ดอก)
    private String lastMonthInterest;
    // สุดทาย(ต้น)
    private String lastMonthPrinciple;
    private String installment = null;
    // รวมสง(ดอก)
    private String totalValueInterest;
    // คงคาง(ดอก)
    private String outStandInterest;
    // รวมสง(ตน)
    private String totalValuePrinciple;
    // คงคาง(ต้น)
    private String outStandPrinciple;

}
