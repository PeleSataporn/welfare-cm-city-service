package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Summary")
public class SummaryDto extends BaseDto {

  private Long sumEmp;
  private Long sumLoan;
  private Long sumLoanBalance;
  private Long sumStockAccumulate;
  private Long sumStockValue;
  private Long sumLoanInterest;
  private Long sumLoanOrdinary;
  private Long sumTotal;
  private String sumMonth;
  private String sumYear;
}
