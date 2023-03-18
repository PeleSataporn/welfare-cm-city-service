package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private GuarantorDto guarantorOne;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private GuarantorDto guarantorTwo;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "loan_id")
  private List<LoanDetailDto> loanDetails;
}
