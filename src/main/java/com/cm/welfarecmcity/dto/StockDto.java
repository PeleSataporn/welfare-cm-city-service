package com.cm.welfarecmcity.dto;

import com.cm.welfarecmcity.dto.base.BaseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Stock")
public class StockDto extends BaseDto {

  @Comment("เงินหุ้นส่งรายเดือน")
  private int stockValue;

  @Comment("หุ้นสะสม")
  private int stockAccumulate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "stock_id")
  private List<StockDetailDto> stockDetails;
}
