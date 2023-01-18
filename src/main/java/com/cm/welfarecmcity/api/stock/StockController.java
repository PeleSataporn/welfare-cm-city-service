package com.cm.welfarecmcity.api.stock;

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

//  @GetMapping
//  public String add() {
//    return "Hello World";
//  }
}
