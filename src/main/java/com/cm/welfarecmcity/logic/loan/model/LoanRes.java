package com.cm.welfarecmcity.logic.loan.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRes {
    private Long id;
    private int loanValue;
    private int loanBalance;
    private int loanTime;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private Boolean stockFlag;
    private String startLoanDate;
    private Long guarantorOne;
    private Long guarantorTwo;
    private int loanOrdinary;
    private int interestPercent;
    private String prefix;
    private String guarantorOneValue;
    private String guarantorTwoValue;
}
