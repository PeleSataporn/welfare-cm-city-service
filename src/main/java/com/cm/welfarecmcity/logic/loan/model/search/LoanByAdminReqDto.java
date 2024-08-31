package com.cm.welfarecmcity.logic.loan.model.search;

public record LoanByAdminReqDto(
        String employeeCode,
        String firstName,
        String lastName,
        String idCard,
        String loanNo,

        // old
         String oldMonth,
                 String oldYear,
                // new
                 String newMonth,
                 String newYear
) {
}
