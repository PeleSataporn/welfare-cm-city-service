package com.cm.welfarecmcity.logic.loan.model;

import lombok.Data;

@Data
public class GuaranteeRes {

    // One
    private String genderOne;
    private String codeGuaranteeOne = null;
    private String fullNameGuaranteeOne = null;
    // Two
    private String genderTwo;
    private String codeGuaranteeTwo = null;
    private String fullNameGuaranteeTwo = null;
}
