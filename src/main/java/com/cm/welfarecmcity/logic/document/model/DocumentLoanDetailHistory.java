package com.cm.welfarecmcity.logic.document.model;

import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.LoanDto;
import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
public class DocumentLoanDetailHistory extends BaseDto {

    private int installment;
    private String loanMonth;
    private String loanYear;
    private int loanOrdinary;
    private int interest;
    private int interestPercent;
    private int interestLastMonth;
    private double loanBalance;
    private Long loanId;
    private Long employeeId;
    private String employeeCode;
    private String loanNo;

}
