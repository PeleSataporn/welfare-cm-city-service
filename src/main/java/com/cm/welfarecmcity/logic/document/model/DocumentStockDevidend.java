package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentStockDevidend {

    private Long empId = null;
    private String departmentName;
    private String employeeCode;
    private String fullName;
    private String bankAccountReceivingNumber;
    private String stockAccumulate;

    private String stockValue;
    private String stockYear;
    private String stockMonth;
    private String interest;
    private String loanYear;
    private String loanMonth;

    // ปนผลหุน
    private String stockDividend;
    // ดอกเบี้ยสะสม
    private String cumulativeInterest;
    // ปนผลดอกเบี้ย
    private String interestDividend;
    // รวมปนผล = ( ปนผลหุน + ปนผลดอกเบี้ย )
    private String totalDividend;


}
