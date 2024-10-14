package com.cm.welfarecmcity.logic.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Data
public class LoanRes {

  private Long id;

  @Comment("เลขสัญญาเงินกู้")
  private String loanNo;

  @Comment("มูลค่าเงินกู้")
  private double loanValue;

  @Comment("เงินกู้คงเหลือ")
  private double loanBalance;

  @Comment("เวลากู้")
  private int loanTime;

  @Comment("ดอกเบี้ย")
  private int interest;

  @Comment("ดอกเบี้ย %")
  private int interestPercent;

  @Comment("สถานะเงินกู้ใหม่")
  private boolean newLoan;

  @JsonIgnore private EmpRes guarantorOne;

  @JsonIgnore private EmpRes guarantorTwo;

  private List<LoanDetailRes> loanDetails;
}
