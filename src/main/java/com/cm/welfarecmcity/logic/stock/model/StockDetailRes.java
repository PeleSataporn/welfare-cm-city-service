package com.cm.welfarecmcity.logic.stock.model;

import com.cm.welfarecmcity.dto.StockDto;
import lombok.Data;
import org.hibernate.annotations.Comment;

@Data
public class StockDetailRes {

  @Comment("ค่าหุ้น(งวดที่)")
  private int installment;

  @Comment("เงินหุ้นส่งรายเดือน")
  private int stockValue;
  private int stockValueDetail;

  @Comment("เดือนที่ส่ง")
  private String stockMonth;

  @Comment("ปีที่ส่ง")
  private String stockYear;

  private Long stockId;
  private Long stockDetailId;

  private int stockAccumulate;
}
