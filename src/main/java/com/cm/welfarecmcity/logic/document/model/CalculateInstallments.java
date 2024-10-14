package com.cm.welfarecmcity.logic.document.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculateInstallments {

  // งวดที่
  private int installment;
  // เงิกู้คงเหลือ
  private int balanceLoan;
  // วันที่หักเงิน
  private String deductionDate;
  // จำนวนวัน
  private String amountDay;
  // ดอกเบี้ย
  private int interest;
  // เงินต้น
  private int principal;
  // เงินต้นคงเหลือ
  private int principalBalance;
  // รวมหัก
  private int totalDeduction;
}
