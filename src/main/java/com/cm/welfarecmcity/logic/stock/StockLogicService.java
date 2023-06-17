package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.logic.stock.model.AddStockDetailAllReq;
import com.cm.welfarecmcity.logic.stock.model.StockRes;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockLogicService {

  @Autowired
  private StockLogicRepository stockRepository;

  @Transactional
  public List<StockRes> searchStock() {
    return stockRepository.searchStock();
  }

  @Transactional
  public ResponseModel<ResponseId> add(AddStockDetailAllReq req) {
    val listStockDetail = stockRepository.getStockDetailByMonth(req.getOldMonth(), req.getOldYear());
    listStockDetail.forEach(detail -> {
      stockRepository.addStockDetailAll(
        req.getNewMonth(),
        req.getNewYear(),
        detail.getInstallment() + 1,
        detail.getStockValue(),
        detail.getStockId()
      );
    });

    return null;
  }
}
