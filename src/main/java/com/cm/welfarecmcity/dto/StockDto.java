package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@Table(name = "Stock")
public class StockDto extends BaseDto {

  @Comment("ค่าหุ้น(งวดที่)")
  private int installment;

  @Comment("ค่าหุ้น(จำนวนเงิน)")
  private int stockValue;

  @Comment("หุ้นสะสม")
  private int stockAccumulate;
}
