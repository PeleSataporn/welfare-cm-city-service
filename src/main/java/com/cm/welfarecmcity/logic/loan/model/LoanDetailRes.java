package com.cm.welfarecmcity.logic.loan.model;

import lombok.Data;
import org.hibernate.annotations.Comment;

@Data
public class LoanDetailRes {
  private Long loanDetailId;

  @Comment("งวดที่")
  private int installment;

  @Comment("ดอกเบี้ย")
  private int interest;

  @Comment("ดอกเบี้ย %")
  private int interestPercent;

  @Comment("เดือนที่ส่ง")
  private String loanMonth;

  @Comment("ปีที่ส่ง")
  private String loanYear;

  @Comment("เงินกู้สามัญ")
  private int loanOrdinary;

  @Comment("ดอกเบี้ยเดือนสุดท้าย")
  private int interestLastMonth;

  private Long loanId;

  @Comment("เวลากู้")
  private int loanTime;

  @Comment("มูลค่าเงินกู้")
  private double loanValue;

  @Comment("สถานะเงินกู้ใหม่")
  private boolean newLoan;

  @Comment("วันที่เริ่มกู้")
  private String startLoanDate;

  private Long employeeId;

  @Comment("เงินกู้คงเหลือ")
  private double loanBalance;
}
