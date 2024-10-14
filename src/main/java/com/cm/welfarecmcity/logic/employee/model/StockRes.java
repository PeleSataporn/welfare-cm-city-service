package com.cm.welfarecmcity.logic.employee.model;

import java.util.List;
import lombok.Data;

@Data
public class StockRes {
  private Long id;
  private List<StockDetailRes> stockDetails;
}
