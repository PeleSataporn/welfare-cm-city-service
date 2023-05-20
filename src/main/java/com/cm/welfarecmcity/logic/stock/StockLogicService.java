package com.cm.welfarecmcity.logic.stock;

import com.cm.welfarecmcity.logic.stock.model.StockRes;
import java.util.List;
import jakarta.transaction.Transactional;
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
}
