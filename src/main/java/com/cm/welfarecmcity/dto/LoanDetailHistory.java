package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "LoanDetailHistory")
public class LoanDetailHistory extends BaseDto {
  @Comment("เงินกู้(งวดที่)")
  private int installment;

  @Comment("เดือนที่ส่ง")
  private String loanMonth;

  @Comment("ปีที่ส่ง")
  private String loanYear;

  @Comment("เงินกู้สามัญ")
  private int loanOrdinary;

  @Comment("ดอกเบี้ย")
  private int interest;

  @Comment("ดอกเบี้ย %")
  private int interestPercent;

  @Comment("ดอกเบี้ยเดือนสุดท้าย")
  private int interestLastMonth;

  @Comment("เงินกู้คงเหลือ")
  private double loanBalance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private LoanDto loan;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private EmployeeDto employee;

  private String employeeCode;

  @Comment("เลขสัญญาเงินกู้")
  private String loanNo;
}
