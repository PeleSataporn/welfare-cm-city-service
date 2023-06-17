package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logic/v1/stock")
public class StockLogicController {

  @Autowired
  private StockLogicService stockService;

  @PostMapping("search")
  public List<StockRes> searchStock() {
    return stockService.searchStock();
  }

  @PostMapping("add-all")
  public ResponseModel<ResponseId> addAll(@RequestBody AddStockDetailAllReq req) {
    return stockService.add(req);
  }
}
