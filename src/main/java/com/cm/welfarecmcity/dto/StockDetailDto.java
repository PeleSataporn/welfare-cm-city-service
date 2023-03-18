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
@Table(name = "StockDetail")
public class StockDetailDto extends BaseDto {

  @Comment("ค่าหุ้น(งวดที่)")
  private int installment;

  @Comment("เงินหุ้นส่งรายเดือน")
  private int stockValue;

  @Comment("เดือนที่ส่ง")
  private String stockMonth;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  private StockDto stock;
}
