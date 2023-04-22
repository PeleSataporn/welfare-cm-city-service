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
@Table(name = "Stock")
public class StockDto extends BaseDto {

  @Comment("เงินหุ้นส่งรายเดือน")
  private int stockValue;

  @Comment("หุ้นสะสม")
  private int stockAccumulate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "stock_id")
  private List<StockDetailDto> stockDetails;

//  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//  private EmployeeDto employee;
}
