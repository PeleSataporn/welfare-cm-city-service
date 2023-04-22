package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.logic.stock.model.StockRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logic/v1/stock")
public class StockLogicController {

  @Autowired
  private StockLogicService stockService;

  @PostMapping("search")
  public List<StockRes> searchStock() {
    return stockService.searchStock();
  }
}
