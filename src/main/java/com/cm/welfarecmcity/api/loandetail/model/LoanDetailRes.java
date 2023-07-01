package com.cm.welfarecmcity.api.loandetail.model;

import lombok.Data;

@Data
public class LoanDetailRes {
    private int installment;
    private String loanMonth;
    private String loanYear;
    private int loanOrdinary;
    private int interest;
    private int interestPercent;
    private int loanBalance;
    private int loanValue;
}
