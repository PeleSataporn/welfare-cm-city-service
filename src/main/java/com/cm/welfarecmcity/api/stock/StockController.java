package com.cm.welfarecmcity.api.stock;

import com.cm.welfarecmcity.api.stock.model.UpdateStockReq;
import com.cm.welfarecmcity.dto.StockDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

  @Autowired
  private StockService stockService;

  @PostMapping
  public ResponseModel<ResponseId> add(@RequestBody StockDto dto) {
    return stockService.add(dto);
  }

  @PatchMapping
  public ResponseModel<ResponseId> update(@RequestBody UpdateStockReq req) {
    return stockService.update(req);
  }

}
