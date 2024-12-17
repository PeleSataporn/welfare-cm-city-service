package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Loan_history")
public class LoanHistoryDto extends BaseDto {

  private Long employeeId;
  private Long loanId;
  private String status;
  private String employeeCode;

}
