package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.dto.base.RequestModel;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.dto.base.SearchDataResponse;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import com.cm.welfarecmcity.logic.stock.model.search.StockByAdminOrderReqDto;
import com.cm.welfarecmcity.logic.stock.model.search.StockByAdminReqDto;
import com.cm.welfarecmcity.utils.response.ResponseDataUtils;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logic/v1/stock")
public class StockLogicController {

  @Autowired private StockLogicService stockService;

  @PostMapping("search")
  public List<StockRes> searchStock() {
    return stockService.searchStock();
  }

  @PostMapping("v2/search")
  public ResponseModel<SearchDataResponse<StockRes>> searchStockByAdmin(
      @RequestBody RequestModel<StockByAdminReqDto, StockByAdminOrderReqDto> req) {
    val res = stockService.searchStockByAdmin(req);
    return ResponseDataUtils.fetchDataSuccess(res);
  }

  @PostMapping("add-all")
  public ResponseModel<ResponseId> addAll(@RequestBody AddStockDetailAllReq req) {
    return stockService.add(req);
  }

  @PostMapping("setting-stock-detail")
  public String settingStockDetailAll(@RequestBody AddStockDetailAllReq req) {
    return stockService.settingStockDetailAll(req);
  }
}
