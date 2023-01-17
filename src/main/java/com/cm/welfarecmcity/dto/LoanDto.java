package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Loan")
public class LoanDto extends BaseDto {

  @Comment("เงินกู้(งวดที่)")
  private int installment;

  @Comment("มูลค่าเงินกู้")
  private int loanValue;

  @Comment("เวลากู้")
  private int loanTime;

  @Comment("ดอกเบี้ย (เปอร์เซ็นต์)")
  private int interestPercent;

  @Comment("เงินกู้สามัญ")
  private int loanOrdinary;

  @Comment("ดอกเบี้ย")
  private int interest;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private GuarantorDto guarantorOne;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private GuarantorDto guarantorTwo;
}
