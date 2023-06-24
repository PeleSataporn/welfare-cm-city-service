package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoanNew {

    private Long empId;
    private String departmentName = null;
    private String employeeCode = null;
    private String fullName = null;
    private String employeeTypeId = null;
    private String employeeTypeName = null;
    private String stockAccumulate = null;
    private String loanValue = null;
    private String loanBalance = null;
    private String interestPercent = null;
    private String salary = null;

    // --------- loan -----------
    private int loanTime;
    private String interestLoan;
    private String guarantorOne;
    private String guarantorTwo;
    private String loanOrdinary = null;
    // เอาไว้เชคตอนใช้หุ้นค้ำ
    private String guaranteeStock;
    private String stockValue;



}
