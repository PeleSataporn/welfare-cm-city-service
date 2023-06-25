package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Loan")
public class LoanDto extends BaseDto {

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

  @Comment("วันที่เริ่มกู้")
  private String startLoanDate;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private EmployeeDto guarantorOne;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private EmployeeDto guarantorTwo;

  //  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  //  private GuarantorDto guarantorOne;
  //
  //  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  //  private GuarantorDto guarantorTwo;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "loan_id")
  private List<LoanDetailDto> loanDetails;
}
