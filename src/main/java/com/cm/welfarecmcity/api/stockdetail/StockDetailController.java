package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock-detail")
public class StockDetailController {

  @Autowired
  private StockDetailService stockDetailService;

  @PostMapping("search-by-stock/{stockId}/{value}")
  public List<StockDetailDto> getStockDetail(@PathVariable Long stockId, @PathVariable String value) {
    return stockDetailService.getStockDetail(stockId, value);
  }
}
