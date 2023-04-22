package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-detail")
public class StockDetailController {

  @Autowired
  private StockDetailService stockDetailService;

  @PostMapping("search-by-stock/{stockId}")
  public List<StockDetailDto> getStockDetail(@PathVariable Long stockId) {
    return stockDetailService.getStockDetail(stockId);
  }
}
