package com.cm.welfarecmcity.api.stockdetail;

import com.cm.welfarecmcity.dto.StockDetailDto;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockDetailService {

  @Autowired
  private StockDetailRepository stockDetailRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  public List<StockDetailDto> getStockDetail(Long stockId) {
    return stockDetailRepository.findAllByStock_Id(stockId);
  }
}
