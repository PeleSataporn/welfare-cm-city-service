package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.api.stock.StockRepository;
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
  private StockLogicRepository stockLogicRepository;

  @Autowired
  private StockRepository stockRepository;

  @Transactional
  public List<StockRes> searchStock() {
    List<StockRes> stock = stockLogicRepository.searchStock();

    for (StockRes item : stock) {
      switch (item.getEmployeeStatus()) {
        case 1 -> item.setStatus("สมาชิกแรกเข้า");
        case 2 -> item.setStatus("ใช้งานปกติ");
        case 3 -> item.setStatus("ลาออก");
        case 4 -> item.setStatus("error");
        case 5 -> item.setStatus("รออนุมัติลาออก");
        case 6 -> item.setStatus("เสียชีวิต");
        case 7 -> item.setStatus("หนีหนี้");
        case 8 -> item.setStatus("เกษียณ");
        default -> item.setStatus("ไม่ทราบสถานะ");
      }
    }

    return stock;
  }

  @Transactional
  public ResponseModel<ResponseId> add(AddStockDetailAllReq req) {
    val listStockDetail = stockLogicRepository.getStockDetailByMonth(req.getOldMonth(), req.getOldYear());
    listStockDetail.forEach(detail -> {
      val stockAccumulate = detail.getStockAccumulate() + detail.getStockValue();

      stockLogicRepository.addStockDetailAll(
        req.getNewMonth(),
        req.getNewYear(),
        detail.getInstallment() + 1,
        detail.getStockValue(),
        detail.getStockId(),
        stockAccumulate
      );

      val stock = stockRepository.findById(detail.getStockId()).get();
      stock.setStockAccumulate(stockAccumulate);
      stockRepository.save(stock);
    });

    return null;
  }
}
