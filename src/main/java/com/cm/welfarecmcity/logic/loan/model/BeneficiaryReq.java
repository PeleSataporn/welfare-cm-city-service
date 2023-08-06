package com.cm.welfarecmcity.logic.loan.model;

import lombok.Data;

@Data
public class BeneficiaryReq {
    private Long id;
    private String prefix;
    private String firstName;
    private String lastName;
    private String gender;
    private String relationship;
    private Boolean active;
    private Long empId;
}
